package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "translations")
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "translation_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    @Column(name = "t_key", length = 150)
    private String tKey;

    // CHANGED: Added columnDefinition = "TEXT" to match your SQL ALTER command
    // @Lob is kept to hint that it is a large object, but columnDefinition ensures it matches the DB schema
    @Lob
    @Column(name = "t_value", nullable = false, columnDefinition = "TEXT")
    private String tValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getTKey() {
        return tKey;
    }

    public void setTKey(String tKey) {
        this.tKey = tKey;
    }

    public String getTValue() {
        return tValue;
    }

    public void setTValue(String tValue) {
        this.tValue = tValue;
    }
}