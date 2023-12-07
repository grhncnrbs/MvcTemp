package com.dhmi.ais.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role {

    @Id
//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_seq")
//  @SequenceGenerator(name = "role_id_seq", sequenceName = "role_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 50, nullable = false)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;
}
