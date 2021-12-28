package demo

import demo.application.domain.Customer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@Suppress("UNUSED_EXPRESSION")
@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
	classes = [DemoApplication::class])
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerControllerTests(@Autowired private val mockMvc: MockMvc) {

	@Test
	fun `GIVEN customer WHEN get is requested THEN a customer is returned`() {

		val expectedCustomer = "{\"id\":\"2\",\"telephone\":\"13255\",\"favorites\":[\"art\"]}"
		mockMvc.get("/customer/2")
			.andExpect {
				status { isOk() }
				content { contentType(MediaType.APPLICATION_JSON) }
				content { json(expectedCustomer) }
			}
	}

	@Test
	fun `GIVEN existing customer WHEN patched THEN only patched fields updated`(){

		val customer = Customer("1","13256465", listOf("music","art"))
		val expectedCustomer = "{\"id\":\"1\",\"telephone\":\"001-555-5678\",\"favorites\":[\"bread\",\"music\",\"art\"]}"

		val patchBody = """[ { "op": "replace", "path": "/telephone", "value": "001-555-5678" },
							{"op": "add", "path": "/favorites/0", "value": "bread" }]"""

		val builder: MockHttpServletRequestBuilder = MockMvcRequestBuilders.patch("/customer/${customer.id}")
			.contentType(MediaType.valueOf("application/json-patch+json"))
			.characterEncoding("UTF-8")
			.content(patchBody)

		this.mockMvc.perform(builder)
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.content().string(expectedCustomer))
			.andDo(MockMvcResultHandlers.print())

		mockMvc.get("/customer/1")
			.andExpect {
				status { isOk() }
				content { contentType(MediaType.APPLICATION_JSON) }
				content { json(expectedCustomer) }
			}
	}
}
