package ch.adesso.crm.rs.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import ch.adesso.crm.persistence.entities.Customer;

@Path("/customers")
@Api(value = "customers", description = "adesso crm customer crud operations")
@Produces(MediaType.APPLICATION_JSON)
public interface ICustomerRestService {

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets a customer with the given id", response = Customer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Invalid ID supplied"),
            @ApiResponse(code = 204, message = "Customer not found")
            })
	Customer getCustomerById(@ApiParam(value = "The id of the user to fetch", required = true) @PathParam("id") Long id);

	@GET
	@Path("/")
	@ApiOperation(value = "Gets all customers registered", response = Customer.class, responseContainer = "List")
	@Produces(MediaType.APPLICATION_JSON)
	List<Customer> getCustomers();

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Saves or updates a customer", response = Customer.class)
	Customer saveOrUpdate(
            @ApiParam(value = "The customer object that is to be updated or saved", required = true) Customer customer);

	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Deletes a customer with the given id")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Invalid ID supplied"),
            @ApiResponse(code = 204, message = "Customer not found")
            })
	void deleteCustomer(@ApiParam(value = "The id of the user to be deleted", required = true) @PathParam("id") Long id);
}
