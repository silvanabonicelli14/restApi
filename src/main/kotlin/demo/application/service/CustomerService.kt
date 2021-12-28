package demo.application.service

import demo.application.domain.Customer
import org.springframework.stereotype.Service


@Service
class CustomerService {

    private var customers: MutableList<Customer> = mutableListOf(
        Customer("1","13256465", listOf("music","art")),
        Customer("2","13255", listOf("art"))
    )

    fun updateCustomer(customer: Customer) {

        val customerToUpdate = findById(customer.id)
        customers[customers.indexOf(customerToUpdate)] = customer
    }

    fun findById(id: String): Customer? =
        customers.firstOrNull { it.id == id }
}
