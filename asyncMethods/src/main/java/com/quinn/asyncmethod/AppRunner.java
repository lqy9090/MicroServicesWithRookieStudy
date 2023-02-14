package com.quinn.asyncmethod;

import com.quinn.asyncmethod.ticketSold.Ticket;
import com.quinn.asyncmethod.ticketSold.TicketSoldWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class AppRunner implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

	private final GitHubLookupService gitHubLookupService;

	private final TicketSoldWindow ticketSoldWindow;

	public AppRunner(GitHubLookupService gitHubLookupService, TicketSoldWindow ticketSoldWindow) {
		this.gitHubLookupService = gitHubLookupService;
		this.ticketSoldWindow = ticketSoldWindow;
	}

	@Override
	public void run(String... args) throws Exception {
//		 Start the clock
		long start = System.currentTimeMillis();

		// Kick of multiple, asynchronous lookups
		CompletableFuture<User> page1 = gitHubLookupService.findUser("PivotalSoftware");
		CompletableFuture<User> page2 = gitHubLookupService.findUser("CloudFoundry");
		CompletableFuture<User> page3 = gitHubLookupService.findUser("Spring-Projects");

		// Wait until they are all done
		CompletableFuture.allOf(page1, page2, page3).join();

		// Print results, including elapsed time
		logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
		logger.info("--> " + page1.get());
		logger.info("--> " + page2.get());
		logger.info("--> " + page3.get());

		//specific order
		CompletableFuture<Object> objectCompletableFuture = gitHubLookupService.findUser("CloudFoundry")
				.thenApply((result) -> {
					logger.info("Specific Order: " + result);
					try {
						CompletableFuture<User> pivotalSoftware = gitHubLookupService.findUser("PivotalSoftware");
						logger.info("Specific Order: " + pivotalSoftware.get());
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}

					return result;
				});
		logger.info("future"+objectCompletableFuture.get());

		//lock
		Ticket ticket = new Ticket(5, true);
		ticketSoldWindow.soldTicket(1, ticket);
		ticketSoldWindow.soldTicket(2, ticket);
		ticketSoldWindow.soldTicket(3, ticket);

	}

}
