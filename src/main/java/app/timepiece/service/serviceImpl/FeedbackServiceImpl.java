package app.timepiece.service.serviceImpl;

import app.timepiece.dto.CreateFeedbackDTO;
import app.timepiece.dto.FeedbackDTO;
import app.timepiece.dto.ShowWatchDTO;
import app.timepiece.entity.Feedback;
import app.timepiece.entity.Order;
import app.timepiece.entity.User;
import app.timepiece.entity.Watch;
import app.timepiece.repository.FeedbackRepository;
import app.timepiece.repository.OrderRepository;
import app.timepiece.repository.UserRepository;
import app.timepiece.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<FeedbackDTO> getAllFeedbacks() {
        List<Feedback> feedbacks =  feedbackRepository.findAll();
        return feedbacks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public FeedbackDTO getFeedbackById(Long id) {
        Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);
        if (optionalFeedback.isPresent()) {
            Feedback feedback = optionalFeedback.get();
            return FeedbackDTO.builder()
                    .id(feedback.getId())
                    .comment(feedback.getComment())
                    .timestamp(feedback.getTimestamp())
                    .rating(feedback.getRating())
                    .userName(feedback.getUser() != null ? feedback.getUser().getName() : null)
                    .orderId(feedback.getOrder() != null ? feedback.getOrder().getId() : null)
                    .parentFeedbackId(feedback.getParentFeedback() != null ? feedback.getParentFeedback().getId() : null)
                    .build();
        }
        return null;
    }

    @Override
    public Feedback updateFeedback(Long id, Feedback feedback) {
        Feedback existingFeedback = feedbackRepository.findById(id).orElse(null);
        if (existingFeedback != null) {
            existingFeedback.setComment(feedback.getComment());
            existingFeedback.setTimestamp(feedback.getTimestamp());
            existingFeedback.setOrder(feedback.getOrder());
            existingFeedback.setParentFeedback(feedback.getParentFeedback());
            existingFeedback.setUser(feedback.getUser());
            return feedbackRepository.save(existingFeedback);
        }
        return null;
    }

    @Override
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    @Override
    public FeedbackDTO createFeedback(CreateFeedbackDTO createFeedbackDTO) {
        Feedback feedback = new Feedback();
        feedback.setComment(createFeedbackDTO.getComment());
        feedback.setTimestamp(Instant.now().toString());
        feedback.setRating(createFeedbackDTO.getRating());

        Optional<User> userOptional = userRepository.findById(createFeedbackDTO.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            feedback.setUser(user);
        } else {
            throw new RuntimeException("User not found");
        }


        Optional<Order> orderOptional = orderRepository.findById(createFeedbackDTO.getOrderId());
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            feedback.setOrder(order);

            // Check if parentFeedbackId is null
            if (createFeedbackDTO.getParentFeedbackId() == null) {
                // Calculate new ratingScore for the seller
                User seller = order.getWatch().getUser();
                double currentRatingScore = seller.getRatingScore() != null ? seller.getRatingScore() : 0.0;
                long numberOfFeedbacks = countFeedbackBySeller(seller.getId());
                double newRatingScore;
                if (numberOfFeedbacks == 0) {
                    newRatingScore = createFeedbackDTO.getRating();
                } else {
                    newRatingScore = (currentRatingScore * numberOfFeedbacks + createFeedbackDTO.getRating()) / (numberOfFeedbacks + 1);
                }
                // Update seller's ratingScore
                seller.setRatingScore(newRatingScore);
            }
            } else {
                throw new RuntimeException("Order not found");
            }

            if (createFeedbackDTO.getParentFeedbackId() != null) {
                Optional<Feedback> parentFeedbackOptional = feedbackRepository.findById(createFeedbackDTO.getParentFeedbackId());
                if (parentFeedbackOptional.isPresent()) {
                    feedback.setParentFeedback(parentFeedbackOptional.get());
                } else {
                    throw new RuntimeException("Parent feedback not found");
                }
            }

            Feedback savedFeedback = feedbackRepository.save(feedback);

        ShowWatchDTO showWatchDTO = null;
        if (savedFeedback.getOrder() != null && savedFeedback.getOrder().getWatch() != null) {
            Watch watch = savedFeedback.getOrder().getWatch();
            showWatchDTO = ShowWatchDTO.builder()
                    .id(watch.getId())
                    .name(watch.getName())
                    .price(watch.getPrice())
                    .status(watch.getStatus())
                    .imageUrl(watch.getImages() != null && !watch.getImages().isEmpty() ? watch.getImages().get(0).getImageUrl() : null)
                    .userId(watch.getUser() != null ? watch.getUser().getId() : null)
                    .userName(watch.getUser() != null ? watch.getUser().getName() : null)
                    .userAvatar(watch.getUser() != null ? watch.getUser().getAvatar() : null)
                    .userRatingScore(watch.getUser() != null ? watch.getUser().getRatingScore() : null)
                    .area(watch.getArea())
                    .address(watch.getAddress())
                    .createDate(watch.getCreateDate())
                    .hasAppraisalCertificate(watch.isHasAppraisalCertificate())
                    .appraisalCertificateUrl(watch.getAppraisalCertificateUrl())
                    .build();
        }

            return FeedbackDTO.builder()
                    .id(savedFeedback.getId())
                    .comment(savedFeedback.getComment())
                    .timestamp(savedFeedback.getTimestamp())
                    .rating(savedFeedback.getRating())
                    .userId(savedFeedback.getUser().getId())
                    .userName(savedFeedback.getUser().getName())
                    .avatar(savedFeedback.getUser().getAvatar())
                    .orderId(savedFeedback.getOrder().getId())
                    .parentFeedbackId(savedFeedback.getParentFeedback() != null ? savedFeedback.getParentFeedback().getId() : null)
                    .watch(showWatchDTO)
                    .build();

    }

    public Long countFeedbackBySeller(Long sellerId) {
        return feedbackRepository.countBySellerId(sellerId);
    }

    @Override
    public List<FeedbackDTO> getFeedbackByOrderId(Long orderId) {
        List<Feedback> feedbackList = feedbackRepository.findByOrderId(orderId);
        Map<Long, FeedbackDTO> feedbackMap = new HashMap<>();

        feedbackList.forEach(feedback -> {
            FeedbackDTO feedbackDTO = FeedbackDTO.builder()
                    .id(feedback.getId())
                    .comment(feedback.getComment())
                    .rating(feedback.getRating())
                    .timestamp(feedback.getTimestamp())
                    .userId(feedback.getUser().getId())
                    .userName(feedback.getUser().getName())
                    .avatar(feedback.getUser().getAvatar())
                    .orderId(feedback.getOrder().getId())
                    .parentFeedbackId(feedback.getParentFeedback() != null ? feedback.getParentFeedback().getId() : null)
                    .build();
            feedbackMap.put(feedback.getId(), feedbackDTO);
        });

        feedbackMap.values().forEach(feedbackDTO -> {
            if (feedbackDTO.getOrderId() != null) {
                Optional<Order> orderOptional = orderRepository.findById(feedbackDTO.getOrderId());
                if (orderOptional.isPresent()) {
                    Order order = orderOptional.get();
                    if (order.getWatch() != null) {
                        Watch watch = order.getWatch();
                        ShowWatchDTO showWatchDTO = ShowWatchDTO.builder()
                                .id(watch.getId())
                                .name(watch.getName())
                                .price(watch.getPrice())
                                .status(watch.getStatus())
                                .imageUrl(watch.getImages() != null && !watch.getImages().isEmpty() ? watch.getImages().get(0).getImageUrl() : null)
                                .userId(watch.getUser() != null ? watch.getUser().getId() : null)
                                .userName(watch.getUser() != null ? watch.getUser().getName() : null)
                                .userAvatar(watch.getUser() != null ? watch.getUser().getAvatar() : null)
                                .userRatingScore(watch.getUser() != null ? watch.getUser().getRatingScore() : null)
                                .area(watch.getArea())
                                .address(watch.getAddress())
                                .createDate(watch.getCreateDate())
                                .hasAppraisalCertificate(watch.isHasAppraisalCertificate())
                                .appraisalCertificateUrl(watch.getAppraisalCertificateUrl())
                                .build();
                        feedbackDTO.setWatch(showWatchDTO);
                    }
                }
            }
        });

        feedbackMap.values().forEach(feedbackDTO -> {
            if (feedbackDTO.getParentFeedbackId() != null) {
                FeedbackDTO parentDTO = feedbackMap.get(feedbackDTO.getParentFeedbackId());
                if (parentDTO != null) {
                    if (parentDTO.getChildFeedbacks() == null) {
                        parentDTO.setChildFeedbacks(new ArrayList<>());
                    }
                    parentDTO.getChildFeedbacks().add(feedbackDTO);
                }
            }
        });

        return feedbackMap.values().stream()
                .filter(feedbackDTO -> feedbackDTO.getParentFeedbackId() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedbackDTO> getFeedbackByWatchId(Long watchId) {
        List<Feedback> feedbackList = feedbackRepository.findFeedbacksByWatchId(watchId);
        Map<Long, FeedbackDTO> feedbackMap = new HashMap<>();

        feedbackList.forEach(feedback -> {
            FeedbackDTO feedbackDTO = FeedbackDTO.builder()
                    .id(feedback.getId())
                    .comment(feedback.getComment())
                    .rating(feedback.getRating())
                    .timestamp(feedback.getTimestamp())
                    .userId(feedback.getUser().getId())
                    .userName(feedback.getUser().getName())
                    .avatar(feedback.getUser().getAvatar())
                    .orderId(feedback.getOrder().getId())
                    .parentFeedbackId(feedback.getParentFeedback() != null ? feedback.getParentFeedback().getId() : null)
                    .build();
            feedbackMap.put(feedback.getId(), feedbackDTO);
        });

        feedbackMap.values().forEach(feedbackDTO -> {
            if (feedbackDTO.getOrderId() != null) {
                Optional<Order> orderOptional = orderRepository.findById(feedbackDTO.getOrderId());
                if (orderOptional.isPresent()) {
                    Order order = orderOptional.get();
                    if (order.getWatch() != null) {
                        Watch watch = order.getWatch();
                        ShowWatchDTO showWatchDTO = ShowWatchDTO.builder()
                                .id(watch.getId())
                                .name(watch.getName())
                                .price(watch.getPrice())
                                .status(watch.getStatus())
                                .imageUrl(watch.getImages() != null && !watch.getImages().isEmpty() ? watch.getImages().get(0).getImageUrl() : null)
                                .userId(watch.getUser() != null ? watch.getUser().getId() : null)
                                .userName(watch.getUser() != null ? watch.getUser().getName() : null)
                                .userAvatar(watch.getUser() != null ? watch.getUser().getAvatar() : null)
                                .userRatingScore(watch.getUser() != null ? watch.getUser().getRatingScore() : null)
                                .area(watch.getArea())
                                .address(watch.getAddress())
                                .createDate(watch.getCreateDate())
                                .hasAppraisalCertificate(watch.isHasAppraisalCertificate())
                                .appraisalCertificateUrl(watch.getAppraisalCertificateUrl())
                                .build();
                        feedbackDTO.setWatch(showWatchDTO);
                    }
                }
            }
        });

        feedbackMap.values().forEach(feedbackDTO -> {
            if (feedbackDTO.getParentFeedbackId() != null) {
                FeedbackDTO parentDTO = feedbackMap.get(feedbackDTO.getParentFeedbackId());
                if (parentDTO != null) {
                    if (parentDTO.getChildFeedbacks() == null) {
                        parentDTO.setChildFeedbacks(new ArrayList<>());
                    }
                    parentDTO.getChildFeedbacks().add(feedbackDTO);
                }
            }
        });

        return feedbackMap.values().stream()
                .filter(feedbackDTO -> feedbackDTO.getParentFeedbackId() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedbackDTO> getFeedbacksBySellerId(Long sellerId) {
        List<Feedback> feedbacks = feedbackRepository.findByParentFeedbackIsNullAndOrder_Watch_UserId(sellerId);
        return feedbacks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private FeedbackDTO convertToDTO(Feedback feedback) {
        FeedbackDTO dto = FeedbackDTO.builder()
                .id(feedback.getId())
                .comment(feedback.getComment())
                .timestamp(feedback.getTimestamp())
                .rating(feedback.getRating())
                .orderId(feedback.getOrder().getId())
                .parentFeedbackId(feedback.getParentFeedback() != null ? feedback.getParentFeedback().getId() : null)
                .build();


        if (feedback.getUser() != null) {
            dto.setUserId(feedback.getUser().getId());
            dto.setUserName(feedback.getUser().getName());
            dto.setAvatar(feedback.getUser().getAvatar());
        }


        if (feedback.getOrder() != null && feedback.getOrder().getWatch() != null) {
            Watch watch = feedback.getOrder().getWatch();
            dto.setWatch(ShowWatchDTO.builder()
                    .id(watch.getId())
                    .name(watch.getName())
                    .price(watch.getPrice())
                    .status(watch.getStatus())
                    .imageUrl(watch.getImages() != null && !watch.getImages().isEmpty() ? watch.getImages().get(0).getImageUrl() : null)
                    .userId(watch.getUser() != null ? watch.getUser().getId() : null)
                    .userName(watch.getUser() != null ? watch.getUser().getName() : null)
                    .userAvatar(watch.getUser() != null ? watch.getUser().getAvatar() : null)
                    .userRatingScore(watch.getUser() != null ? watch.getUser().getRatingScore() : null)
                    .area(watch.getArea())
                    .address(watch.getAddress())
                    .createDate(watch.getCreateDate())
                    .hasAppraisalCertificate(watch.isHasAppraisalCertificate())
                    .appraisalCertificateUrl(watch.getAppraisalCertificateUrl())
                    .build());
        }
        return dto;
    }
}
