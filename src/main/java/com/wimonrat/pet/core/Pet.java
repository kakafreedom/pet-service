package com.wimonrat.pet.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.URL;
import org.joda.time.DateTime;

import javax.persistence.*;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pets")
@NamedQueries({
        @NamedQuery(
                name = "Pet.findById",
                query = "SELECT p FROM Pet p where p.id = :id and p.isDeleted = false"
        )
})
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PetType type;

    private Integer age;

    private Sex sex;

    private String description;

    @JsonProperty("owner_email")
    @Column(name = "owner_email")
    @Email(message = "invalid email address.")
    private String ownerEmail;

    @JsonProperty("image_url")
    @Column(name = "image_url")
    @URL(message = "invalid url format.")
    private String imageUrl;

    @JsonIgnore
    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "created_date")
    private DateTime createdDate;

    @Column(name = "updated_date")
    private DateTime updatedDate;


    @PrePersist
    protected void onCreate() {
        this.createdDate = new DateTime();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new DateTime();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Pet pet = (Pet) other;
        return Objects.equal(this.id, pet.getId()) &&
                getType() == pet.getType() &&
                Objects.equal(getAge(), pet.getAge()) &&
                getSex() == pet.getSex() &&
                isDeleted() == pet.isDeleted() &&
                Objects.equal(getDescription(), pet.getDescription()) &&
                Objects.equal(getOwnerEmail(), pet.getOwnerEmail()) &&
                Objects.equal(getImageUrl(), pet.getImageUrl()) &&
                Objects.equal(getCreatedDate(), pet.getCreatedDate()) &&
                Objects.equal(getUpdatedDate(), pet.getUpdatedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getType(), getAge(), getSex(), isDeleted(), getDescription(),
                getOwnerEmail(), getImageUrl(), getCreatedDate(), getUpdatedDate());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}