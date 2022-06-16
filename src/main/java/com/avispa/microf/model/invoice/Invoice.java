package com.avispa.microf.model.invoice;

import com.avispa.ecm.model.document.Document;
import com.avispa.microf.model.bankaccount.BankAccount;
import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.invoice.position.Position;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Rafał Hiszpański
 */
@Entity
@Getter
@Setter
public class Invoice extends Document {
    private static final String INVOICE_NUMBER_TEMPLATE = "F/%d/%s/%s";

    @Column(name = "serial_number")
    private Integer serialNumber;

    @ManyToOne(optional = false)
    private Customer seller;

    @ManyToOne(optional = false)
    private Customer buyer;

    @Column(name = "issue_date", columnDefinition = "DATE")
    private LocalDate issueDate;

    @Column(name = "service_date", columnDefinition = "DATE")
    private LocalDate serviceDate;

    @OrderColumn
    @OneToMany(cascade = CascadeType.ALL)
    private List<Position> positions;

    @ManyToOne(optional = false)
    private BankAccount bankAccount;

    private String comments;
}
