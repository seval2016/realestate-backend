package com.project.entity.concretes.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class CategoryPropertyKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  //  @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean builtIn = false;

    // 1. Her özelliğin bir kategoriye ait olması için ilişki
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private Long managerId ;





}