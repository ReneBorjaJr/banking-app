package com.rene.bankingapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String status;

    @NotNull
    private String payee;
    @NotNull
    private String nickname;

    @NotNull
    private Date creationDate;
    @NotNull
    private Date paymentDate;
    @NotNull
    private Integer recurringDate;
    @NotNull
    private Date upcomingPaymentDate;

    @NotNull
    private Double paymentAmount;

//    @NotNull
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "account_id")
//    private Long accountId;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    @NotNull
    public Bill() {
    }

    public Bill(Account account, Date creationDate, Long id, String nickname, String payee, Double paymentAmount, Date paymentDate, Integer recurringDate, String status, Date upcomingPaymentDate) {
        this.account = account;
        this.creationDate = creationDate;
        this.id = id;
        this.nickname = nickname;
        this.payee = payee;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
        this.recurringDate = recurringDate;
        this.status = status;
        this.upcomingPaymentDate = upcomingPaymentDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Integer getRecurringDate() {
        return recurringDate;
    }

    public void setRecurringDate(Integer recurringDate) {
        this.recurringDate = recurringDate;
    }

    public Date getUpcomingPaymentDate() {
        return upcomingPaymentDate;
    }

    public void setUpcomingPaymentDate(Date upcomingPaymentDate) {
        this.upcomingPaymentDate = upcomingPaymentDate;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

//    public Long getAccountId() {
//        return accountId;
//    }
//
//    public void setAccountId(Long accountId) {
//        this.accountId = accountId;
//    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
