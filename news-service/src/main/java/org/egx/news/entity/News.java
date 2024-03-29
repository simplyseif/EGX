package org.egx.news.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String title;
    @Column(columnDefinition="TEXT")
    private String article;
    private String newsDate;
    private String newsTime;
    @ManyToOne
    @JoinColumn(name = "equity_id")
    private Equity equity;
}
