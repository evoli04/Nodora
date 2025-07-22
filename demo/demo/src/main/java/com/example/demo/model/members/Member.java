package com.example.demo.model.members;

import java.util.Objects;

public class Member { // Sınıf ismi büyük harfle başlamalı
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private Boolean isAdmin;
    private String membername;

    // No-args constructor
    public Member() {
    }

    // All-args constructor
    public Member(Integer id, String name, String surname, String email,
                  String password, Boolean isAdmin, String membername) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.membername = membername;
    }

    // Getter ve Setter metodları
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                ", isAdmin=" + isAdmin +
                ", membername='" + membername + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) &&
                Objects.equals(name, member.name) &&
                Objects.equals(surname, member.surname) &&
                Objects.equals(email, member.email) &&
                Objects.equals(password, member.password) &&
                Objects.equals(isAdmin, member.isAdmin) &&
                Objects.equals(membername, member.membername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, email, password, isAdmin, membername);
    }
}