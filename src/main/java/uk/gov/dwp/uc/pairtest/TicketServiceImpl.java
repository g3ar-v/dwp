package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */
    private final int MAX_TICKETS = 20;

    // Real life scenario these would be floats for security reasons
    private final int ADULT_PRICE = 20;
    private final int CHILD_PRICE = 10;

    private int numOfTickets = 0;
    private int totalPrice;
    private int totalSeatsAllocated;

    private TicketTypeRequest[] tickets;
    TicketPaymentServiceImpl ticketPaymentService;
    SeatReservationServiceImpl seatReservationService;

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests)
            throws InvalidPurchaseException {
        if (accountId > 0) {
            this.tickets = ticketTypeRequests; // assumption that the argument can be stored in an array like this
            if (this.containsAdult(tickets)) {
                if (this.getNumberOfTickets(tickets) <= MAX_TICKETS) {

                    this.totalPrice = getTotalPrice(ticketTypeRequests);
                    ticketPaymentService.makePayment(accountId, this.totalPrice);

                    this.totalSeatsAllocated = getNumberOfSeats(ticketTypeRequests);
                    seatReservationService.reserveSeat(accountId, this.totalSeatsAllocated);
                }

            } else {
                throw new InvalidPurchaseException();
            }

        } else {
            throw new InvalidPurchaseException();
        }

    }

    private boolean containsAdult(TicketTypeRequest[] tickets) {
        // optimise search on array

        for (int i = 0; i < tickets.length; i++) {
            if (tickets[i].getTicketType() == TicketTypeRequest.Type.ADULT) {
                return true;
            }
        }
        return false;
    }

    private int getTotalPrice(TicketTypeRequest[] tickets) {
        int totalPrice = 0;
        for (int i = 0; i < tickets.length; i++) {
            switch (tickets[i].getTicketType()) {
                case CHILD:
                    totalPrice += CHILD_PRICE * tickets[i].getNoOfTickets();
                    break;
                case ADULT:
                    totalPrice += ADULT_PRICE * tickets[i].getNoOfTickets();
                    break;
                default:
                    throw new InvalidPurchaseException();
            }

        }
        return this.totalPrice = totalPrice;

    }

    /*
     * This implementation entails that The TicketService is called everytime a user
     * wants to make a payment
     * I could make it return a varying value everytime its called depending on the
     * change in tickets
     */
    private int getNumberOfTickets(TicketTypeRequest[] tickets) {
        for (int i = 0; i < tickets.length; i++) {
            this.numOfTickets += tickets[i].getNoOfTickets();
        }
        return this.numOfTickets;

    }

    private int getNumberOfSeats(TicketTypeRequest[] tickets) {
        int totalSeatsAllocated = 0;
        for (int i = 0; i < tickets.length; i++) {
            if (!(tickets[i].getTicketType() == TicketTypeRequest.Type.INFANT)) {
                totalSeatsAllocated += 1;
            }
        }
        return totalSeatsAllocated;
    }

}
