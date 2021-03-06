package demo.doors.routes

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import com.github.fge.jsonpatch.JsonPatchException
import demo.application.domain.Customer
import demo.application.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class CustomerController {
    private var customerService = CustomerService()
    private val objectMapper = ObjectMapper()

    @GetMapping("/customer/{id}")
    fun getCustomer(@PathVariable id: String):  ResponseEntity<Customer?>? {
        return ResponseEntity.ok(customerService.findById(id))
    }

    @PatchMapping(path = ["/customer/{id}"], consumes = ["application/json-patch+json"])
    fun updateCustomer(@PathVariable id: String, @RequestBody patch: JsonPatch): ResponseEntity<Customer?>? {
        return try {
            customerService.findById(id)?.let {
                val customerPatched: Customer = applyPatchToCustomer(patch, it)
                customerService.updateCustomer(customerPatched)
                ResponseEntity.ok(customerPatched)
            }
        } catch (e: JsonPatchException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        } catch (e: JsonProcessingException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @Throws(JsonPatchException::class, JsonProcessingException::class)
    private fun applyPatchToCustomer(
        patch: JsonPatch, targetCustomer: Customer
    ): Customer {
        val patched = patch.apply(objectMapper.convertValue(targetCustomer, JsonNode::class.java))
        return objectMapper.treeToValue(patched, Customer::class.java)
    }
}
