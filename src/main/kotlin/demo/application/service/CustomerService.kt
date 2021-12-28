package demo.application.service

import demo.application.domain.Customer
import org.springframework.stereotype.Service
import java.util.*


@Service
class CustomerService {

    private val customers: MutableList<Customer> = ArrayList()

    fun updateCustomer(customer: Customer) {
        customers[customers.indexOf(customer)] = customer
    }
}
