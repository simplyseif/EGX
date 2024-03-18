package org.egx.news.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.egx.clients.io.BaseEquity;

import java.util.List;

@Entity
@Table(name = "equity")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Equity extends BaseEquity {
    @Builder
    public Equity(Integer id, String reutersCode, String name, String ISN, String sector, String listingDate) {
        super(id, reutersCode, name, ISN, sector, listingDate);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "equity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<News> news;

}
