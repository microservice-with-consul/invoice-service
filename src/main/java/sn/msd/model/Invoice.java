package sn.msd.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVReader;

@RestController
@RequestMapping("invoices")
@RefreshScope
public class Invoice {
	private String id;
	private String branch;
	private String city;
	private String customerType;
	private String gender;
	private String productLigne;
	private Double unitPrice;
	private Integer quantity;
	private Double tax;
	private Double total;
	private Date date;
	private static List<Invoice> invoices;
	
	@Value("${invoice.price}")
	public String invoicePrice;
	
	@Value("${invoice.unit}")
	public String invoiceUnit;

	public Invoice() {
		super();
	}

	public Invoice(String id, String branch, String city, String customerType, String gender, String productLigne,
			Double unitPrice, Integer quantity, Double tax, Double total, Date date) {
		super();
		this.id = id;
		this.branch = branch;
		this.city = city;
		this.customerType = customerType;
		this.gender = gender;
		this.productLigne = productLigne;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
		this.tax = tax;
		this.total = total;
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getProductLigne() {
		return productLigne;
	}

	public void setProductLigne(String productLigne) {
		this.productLigne = productLigne;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Invoice [id=" + id + ", branch=" + branch + ", city=" + city + ", customerType=" + customerType
				+ ", gender=" + gender + ", productLigne=" + productLigne + ", unitPrice=" + unitPrice + ", quantity="
				+ quantity + ", tax=" + tax + ", total=" + total + ", date=" + date + "]";
	}

	// Chargement des factures à partir d'un fichier source
	static {
		invoices = new ArrayList<Invoice>();
		try (CSVReader csvReader = new CSVReader(new FileReader("src/main/resources/static/supermarket_sales.csv"));) {
			String[] values = null;
			while ((values = csvReader.readNext()) != null) {
				if (csvReader.getLinesRead() != 1) {
					Invoice invoice = new Invoice();
					invoice.setId(values[0]);
					invoice.setBranch(values[1]);
					invoice.setCity(values[2]);
					invoice.setCustomerType(values[3]);
					invoice.setGender(values[4]);
					invoice.setProductLigne(values[5]);
					invoice.setUnitPrice(Double.parseDouble(values[6]));
					invoice.setQuantity(Integer.parseInt(values[7]));
					invoice.setTax(Double.parseDouble(values[8]));
					invoice.setTotal(Double.parseDouble(values[9]));
					invoice.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(values[10]));

					invoices.add(invoice);
				}
			}
		} catch (FileNotFoundException fe) {
			fe.getMessage();
		} catch (IOException ioe) {
			ioe.getMessage();
		} catch (ParseException pe) {
			pe.getMessage();
		}
	}

	// Liste de toutes les factures
	@GetMapping("list")
	public List<Invoice> getAllInvoice() {
		return invoices;
	}
	
	@GetMapping("params")  
	public Map<String, String> getParams() {
		Map<String, String> map = new HashMap<>();
		map.put("Invoice unit", invoiceUnit);
		map.put("Invoice price", invoicePrice);
		
		return map;
	}
}
