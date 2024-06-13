package com.rene.bankingapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
@Entity
public class Account {
 @Id
 @GeneratedValue
 @Column(name = "ACCOUNT_ID")
 private Long id;

 private String type;
 private String nickname;
 private Integer rewards;
 private Double balance;
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
