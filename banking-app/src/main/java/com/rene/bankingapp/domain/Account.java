package com.rene.bankingapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Account {
 @Id
 @GeneratedValue
 @Column(name = "ACCOUNT_ID")
 private Long id;
 @NotBlank
 @Enumerated(EnumType.STRING)
 private String type;
 @NotBlank
 @Size(min = 3, max = 20)
 private String nickname;
 @Min(0)
 @Column(name = "member_points")
 private Integer rewards;
 @Min(0)
 private Double balance;
 @NotNull
 @ManyToOne(fetch = FetchType.LAZY)
 private Customer customer;

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getType() {
  return type;
 }

 public void setType(String type) {
  this.type = type;
 }

 public String getNickname() {
  return nickname;
 }

 public void setNickname(String nickname) {
  this.nickname = nickname;
 }

 public Integer getRewards() {
  return rewards;
 }

 public void setRewards(Integer rewards) {
  this.rewards = rewards;
 }

 public Double getBalance() {
  return balance;
 }

 public void setBalance(Double balance) {
  this.balance = balance;
 }

 public Customer getCustomer() {
  return customer;
 }

 public void setCustomer(Customer customer) {
  this.customer = customer;
 }




}
