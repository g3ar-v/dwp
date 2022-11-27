package uk.gov.dwp.uc.pairtest;

import java.util.logging.Level;
import java.util.logging.Logger;

import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */
    private final int MAXTICKETS = 20;
    Logger logger = Logger.getLogger(this.getClass().getName());

    // Real life scenario these would be floats for security reasons
    private final int ADULTPRICE = 20;
    private final int CHILDPRICE = 10;
    Long accountId;

    private int numOfTickets = 0;
    private int totalPrice;
    private int totalSeatsAllocated;

    private TicketTypeRequest[] tickets;
    TicketPaymentServiceImpl ticketPaymentService;
    SeatReservationServiceImpl seatReservationService;

    @Override
    public final void purchaseTickets(final Long accountId, final TicketTypeRequest... ticketTypeRequests)
            throws InvalidPurchaseException {
        logger.setLevel(Level.OFF);
        if (accountId > 0) {

            this.tickets = ticketTypeRequests; // assumption that the argument can be stored in an array like this
            if (this.containsAdult(tickets)) {
                if (this.calculateNumberOfTickets(tickets) <= MAXTICKETS) {

                    this.totalPrice = calculateTotalPrice(ticketTypeRequests);
                    // ticketPaymentService.makePayment(accountId, this.totalPrice);

                    this.totalSeatsAllocated = calculateNumberOfSeats(ticketTypeRequests);
                    // seatReservationService.reserveSeat(accountId, this.totalSeatsAllocated);
                }

            } else {
                throw new InvalidPurchaseException();
            }

        } else {
            throw new InvalidPurchaseException();
        }
    }

    public int getPrice() {
        return this.totalPrice;
    }

    public int getSeats() {
        return this.totalSeatsAllocated;
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

    private int calculateTotalPrice(TicketTypeRequest[] tickets) {
        logger.log(Level.INFO, "In calculateTotalPrice method");
        int totalPrice = 0;
        for (int i = 0; i < tickets.length; i++) {
            logger.log(Level.INFO, tickets[i].getTicketType().toString());
            switch (tickets[i].getTicketType()) {
                case CHILD:
                    totalPrice += CHILDPRICE * tickets[i].getNoOfTickets();
                    break;
                case ADULT:
                    totalPrice += ADULTPRICE * tickets[i].getNoOfTickets();
                    break;
                default:
                    throw new InvalidPurchaseException();
            }

        }
        logger.log(Level.INFO, Integer.toString(totalPrice));
        return this.totalPrice = totalPrice;

    }

    /*
     * This implementation entails that The TicketService is called everytime a user
     * wants to make a payment
     * I could make it return a varying value everytime its called depending on the
     * change in tickets
     */
    private int calculateNumberOfTickets(TicketTypeRequest[] tickets) {
        for (int i = 0; i < tickets.length; i++) {
            this.numOfTickets += tickets[i].getNoOfTickets();
        }
        return this.numOfTickets;

    }

    private int calculateNumberOfSeats(TicketTypeRequest[] tickets) {
        int totalSeatsAllocated = 0;
        for (int i = 0; i < tickets.length; i++) {
            if (!(tickets[i].getTicketType() == TicketTypeRequest.Type.INFANT)) {
                totalSeatsAllocated += 1 * tickets[i].getNoOfTickets();
            }
        }
        logger.log(Level.INFO, "out of calculateNumberOfSeats");
        return totalSeatsAllocated;
    }

}
