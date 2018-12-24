package lambda.domain;

public class Tax {
    private Double taxrate;



    Tax(Double taxrate) {
        this.taxrate = taxrate;
    }

    public Double getTaxtate(Double income) {

        return income * taxrate;
    }
}
