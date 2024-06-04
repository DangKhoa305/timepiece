package app.timepiece.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "RequestImage")
public class RequestImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appraisal_request_id", nullable = false)
    private AppraisalRequest appraisalRequest;

    @Column(nullable = false)
    private String imageUrl;


}
