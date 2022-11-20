package com.example.accountservice.service

import com.example.accountservice.model.Account
import com.example.accountservice.repository.AccountRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountServiceImpl(val accountRepository: AccountRepository) : AccountService {

    @CacheEvict(value = ["accounts"], allEntries = true)
    override fun getAmount(id: Int): Long {
        println("client!")
        if (accountRepository.existsById(id)) {
            return accountRepository.getReferenceById(id).value
        } else throw NullPointerException()
    }

    @Transactional
    @CachePut(value = ["accounts"], key = "#id")
    override fun addAmount(id: Int, value: Long) {
        if (accountRepository.existsById(id)) {
            val account = accountRepository.getReferenceById(id)
            accountRepository.save(Account(id, account.value + value))
        } else {
            accountRepository.save(Account(id, value))
        }
    }

}
