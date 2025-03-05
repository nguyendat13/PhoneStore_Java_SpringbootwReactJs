package com.backend.backend_java.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reply_contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    private String replyContent;
    private String createdAt;

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Người phản hồi
}
