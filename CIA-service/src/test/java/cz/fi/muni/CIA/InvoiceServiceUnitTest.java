package cz.fi.muni.CIA;

import cz.fi.muni.CIA.entities.Address;
import cz.fi.muni.CIA.entities.Invoice;
import cz.fi.muni.CIA.entities.InvoiceType;
import cz.fi.muni.CIA.entities.Person;
import cz.fi.muni.CIA.managers.InvoiceManager;
import cz.fi.muni.CIA.managers.PersonManager;
import org.mockito.InjectMocks;
import org.springframework.test.context.TestExecutionListeners;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * class InvoiceServiceUnitTest
 *
 * @author Peter Balcirak <peter.balcirak@gmail.com>
 */
public class InvoiceServiceUnitTest {

	private InvoiceManager invoiceManager = mock(InvoiceManager.class);

	@InjectMocks
	private InvoiceService invoiceService;

	private Person person;
	private Invoice invoiceOne;
	private Invoice invoiceTwo;

	@BeforeMethod
	public void setService() {
		reset(invoiceManager);
		invoiceService = new InvoiceServiceImpl(invoiceManager);
	}

	@BeforeMethod
	public void setEntities() {
		Address address = new Address();
		address.setCity("malinova");
		address.setCountry("SR");
		address.setPostCode("61400");
		address.setStreetAddress("none");

		person = new Person();
		person.setEmail("emailOne");
		person.setAccountNumber("1111");
		person.setPhoneNumber("11111");
		person.setName("peter");
		person.setAddress(address);

		invoiceOne = new Invoice();
		invoiceOne.setInvoiceType(InvoiceType.EXPENSE);
		invoiceOne.setItems(new HashSet());
		invoiceOne.setPrice(10D);
		invoiceOne.setPayer(person);
		invoiceOne.setRecipient(person);
		invoiceOne.setDueDate(LocalDate.of(2018, 6, 10));
		invoiceOne.setIssueDate(LocalDate.of(2018, 5, 10));

		invoiceTwo = new Invoice();
		invoiceTwo.setId(2L);
		invoiceTwo.setInvoiceType(InvoiceType.INCOME);
		invoiceTwo.setItems(new HashSet());
		invoiceTwo.setPrice(20D);
		invoiceTwo.setPayer(person);
		invoiceTwo.setRecipient(person);
		invoiceTwo.setDueDate(LocalDate.of(2017, 6, 10));
		invoiceTwo.setIssueDate(LocalDate.of(2017, 5, 10));
	}

	@Test
	public void testCreateInvoice() {
		invoiceService.createInvoice(invoiceOne);

		verify(invoiceManager, times(1)).addInvoice(invoiceOne);
	}

	@Test
	public void testDeleteInvoice() {
		invoiceService.deleteInvoice(1L);

		verify(invoiceManager, times(1)).deleteInvoice(1L);
	}

	@Test
	public void testEditInvoice() {
		invoiceService.editInvoice(invoiceTwo);

		verify(invoiceManager, times(1)).updateInvoice(invoiceTwo);
	}

	@Test
	public void testGetAllInvoices() {
		invoiceService.getAllInvoices();

		verify(invoiceManager, times(1)).getAll();
	}

	@Test
	public void testGetAllExpenses() {
		invoiceService.getInvoicesByType(InvoiceType.EXPENSE);

		verify(invoiceManager, times(1)).getAllExpenses();
	}

	@Test
	public void testGetAllIncomes() {
		invoiceService.getInvoicesByType(InvoiceType.INCOME);

		verify(invoiceManager, times(1)).getAllIncomes();
	}

	@Test
	public void testGetInvoiceById() {
		invoiceService.getInvoiceById(1L);

		verify(invoiceManager, times(1)).findById(1L);
	}

	@Test
	public void testGetInvoiceByPersonIdAndDate() {
		invoiceService.getInvoicesByPersonIdAndDate(LocalDate.of(2016, 1, 1), LocalDate.of(2019, 1, 1), 1L);

		verify(invoiceManager, times(1)).getAll();
	}

	@Test
	public void testGetInvoiceByTypeAndDate() {
		invoiceService.getInvoicesByTypeAndDate(LocalDate.of(2016, 1, 1), LocalDate.of(2019, 1, 1), InvoiceType.EXPENSE);

		verify(invoiceManager, times(1)).getAllExpenses();
	}

	@Test
	public void testGetInvoicesByPersonIdAndTypeAndDate() {
		invoiceService.getInvoicesByPersonIdAndTypeAndDate(LocalDate.of(2016, 1, 1), LocalDate.of(2019, 1, 1), 1L, InvoiceType.INCOME);

		verify(invoiceManager, times(1)).getAllIncomes();
	}

	@Test
	public void testGetInvoicesInDateInterval() {
		invoiceService.getInvoicesInDateInterval(LocalDate.of(2016, 1, 1), LocalDate.of(2019, 1, 1));

		verify(invoiceManager, times(1)).getAll();
	}

	@Test (expectedExceptions=IllegalArgumentException.class)
	public void testCreateInvoiceWithId() {
		invoiceService.createInvoice(invoiceTwo);
	}

	@Test (expectedExceptions=IllegalArgumentException.class)
	public void testDeleteInvoiceWithNegativeId() {
		invoiceService.deleteInvoice(-1L);
	}

	@Test (expectedExceptions=IllegalArgumentException.class)
	public void testEditInvoiceWithNullId() {
		invoiceService.editInvoice(invoiceOne);
	}

	@Test (expectedExceptions=IllegalArgumentException.class)
	public void testGetAllInvoicesForNullTYpe() {
		invoiceService.getInvoicesByType(null);
	}

	@Test (expectedExceptions=IllegalArgumentException.class)
	public void testGetInvoicesByTypeAndNullDate() {
		invoiceService.getInvoicesByTypeAndDate(null, null, InvoiceType.EXPENSE);
	}

	@Test (expectedExceptions=IllegalArgumentException.class)
	public void testGetInvoicesInNullDateInterval() {
		invoiceService.getInvoicesInDateInterval(null, null);
	}

	@Test (expectedExceptions=IllegalArgumentException.class)
	public void testGetInvoicesByNegativePersonIdAndTypeAndDate() {
		invoiceService.getInvoicesByPersonIdAndTypeAndDate(LocalDate.of(2016, 1, 1), LocalDate.of(2019, 1, 1),-1L, InvoiceType.EXPENSE);
	}

	@Test (expectedExceptions=IllegalArgumentException.class)
	public void testGetInvoicesByPersonIdAndNullDate() {
		invoiceService.getInvoicesByPersonIdAndDate(null, null, 1L);
	}
}
