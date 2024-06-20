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
    @Column(name = "status")
    private String status;

    @NotNull
    @Column(name = "payee")
    private String payee;

    @NotNull
    @Column(name = "nickname")
    private String nickname;

    @NotNull
    @Column(name = "creation_date")
    private Date creationDate;

    @NotNull
    @Column(name = "payment_date")
    private Date paymentDate;

    @NotNull
    @Column(name = "recurring_date")
    private Integer recurringDate;

    @NotNull
    @Column(name = "upcoming_payment_date")
    private Date upcomingPaymentDate;

    @NotNull
    @Column(name = "payment_amount")
    private Double paymentAmount;

    @NotNull
    @Column(name = "account_id")
    private Long accountId;


    @NotNull
    public Bill() {
    }

    public Bill(String status, String payee, String nickname, Date creationDate, Date paymentDate,
                Integer recurringDate, Date upcomingPaymentDate, Double paymentAmount, Long accountId) {
        this.status = status;
        this.payee = payee;
        this.nickname = nickname;
        this.creationDate = creationDate;
        this.paymentDate = paymentDate;
        this.recurringDate = recurringDate;
        this.upcomingPaymentDate = upcomingPaymentDate;
        this.paymentAmount = paymentAmount;
        this.accountId = accountId;
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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
