package com.example.accountservice.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "account")
class Account(
    @Id
    var id: Int,
    var value: Long
)