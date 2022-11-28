package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.Assert.*;

import org.junit.Test;

public class testTicketServiceImpl {
	TicketServiceImpl ticketService;

	TicketTypeRequest childTicket;
	TicketTypeRequest adultTicket;
	TicketTypeRequest infantTicket;

	@Test
	public void testTotalPrice() {

		Long accountId = 200000L;
		childTicket = new TicketTypeRequest(Type.CHILD, 4);
		adultTicket = new TicketTypeRequest(Type.ADULT, 4);

		infantTicket = new TicketTypeRequest(Type.ADULT, 2);

		ticketService = new TicketServiceImpl();
		ticketService.purchaseTickets(accountId, childTicket, adultTicket);

		/* Test for calculations */
		assertEquals(120, ticketService.getPrice());

	}

	@Test
	public void testNumberOfSeatsAllocated() {

		Long accountId = 200000L;
		childTicket = new TicketTypeRequest(Type.CHILD, 4);
		adultTicket = new TicketTypeRequest(Type.ADULT, 2);
		infantTicket = new TicketTypeRequest(Type.ADULT, 2);

		ticketService = new TicketServiceImpl();
		ticketService.purchaseTickets(accountId, childTicket, adultTicket);

		assertEquals(6, ticketService.getSeats());

	}

	@Test(expected = InvalidPurchaseException.class)
	public void testInvalidNumber() {
		Long accountId = 23004L;
		childTicket = new TicketTypeRequest(Type.CHILD, 2);
		adultTicket = new TicketTypeRequest(Type.ADULT, 2);
		infantTicket = new TicketTypeRequest(Type.INFANT, 2);

		ticketService = new TicketServiceImpl();
		ticketService.purchaseTickets(accountId, adultTicket, childTicket, infantTicket);

	}

	@Test
	public void testForNoAdult() {

	}

	// @Test
	// public void testForNoAdult() {

	// }

	// public static void main(String[] args) {
	// Long accountId = 200000L;
	// TicketServiceImpl ticketService;
	// TicketTypeRequest childTicket = new TicketTypeRequest(Type.CHILD, 4);
	// TicketTypeRequest adultTicket = new TicketTypeRequest(Type.ADULT, 2);

	// ticketService = new TicketServiceImpl();

	// ticketService.purchaseTickets(accountId, childTicket, adultTicket);
	// assertEquals(80, ticketService.getPrice());
	// }

}