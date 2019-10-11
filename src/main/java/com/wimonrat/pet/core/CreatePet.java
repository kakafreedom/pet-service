package com.wimonrat.pet.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePet {

    @NotNull(message = "Pet type may not be null.")
    private PetType type;

    @Min(value = 1, message = "Minimum value for age is 1.")
    private int age;

    @NotNull(message = "Sex may not be null")
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


    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        CreatePet pet = (CreatePet) other;
        return pet.getType() == pet.getType() &&
                Objects.equal(getAge(), pet.getAge()) &&
                pet.getSex() == pet.getSex() &&
                Objects.equal(getDescription(), pet.getDescription()) &&
                Objects.equal(getOwnerEmail(), pet.getOwnerEmail()) &&
                Objects.equal(getImageUrl(), pet.getImageUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getType(), getAge(), getSex(), getDescription(),
                getOwnerEmail(), getImageUrl());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public static Pet toPet(CreatePet createPet) {
        return Pet.builder().sex(createPet.getSex())
                .type(createPet.getType())
                .age(createPet.getAge())
                .description(createPet.getDescription())
                .ownerEmail(createPet.getOwnerEmail())
                .imageUrl(createPet.getImageUrl())
                .build();
    }
}