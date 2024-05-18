package com.ezybytes.account.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "accounts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Accounts extends BaseEntity {

    @Id
    private Long accountNumber;
    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "account_type")
    private String accountType;
    @Column(name = "branch_address")
    private String branchAddress;
}
