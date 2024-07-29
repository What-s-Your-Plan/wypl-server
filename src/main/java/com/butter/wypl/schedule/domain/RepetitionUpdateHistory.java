package com.butter.wypl.schedule.domain;

import com.butter.wypl.global.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "repetition_update_history_tbl")
public class RepetitionUpdateHistory extends BaseEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repetition_id", nullable = false)
    private Repetition repetition;

}
