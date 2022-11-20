package com.example.accountservice.controller

import com.example.accountservice.model.AccountRq
import com.example.accountservice.model.AccountRs
import com.example.accountservice.service.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("account")
class AmountController(val accountService: AccountService) {

    @PostMapping
    fun addAmount(@RequestBody account: AccountRq) : ResponseEntity<String> {
        accountService.addAmount(account.id, account.value)
        return ResponseEntity.ok("Success")
    }

    @GetMapping("/{id}")
    fun getAmount(@PathVariable id: Int) : ResponseEntity<AccountRs>  {
        return ResponseEntity.ok(AccountRs(accountService.getAmount(id)))
    }

}