package softuni.exam.models.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "offers")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferSeedRootDto {

    @XmlElement(name = "offer")
    private List<OfferRootDto> offers;

    public OfferSeedRootDto() {
    }

    public List<OfferRootDto> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferRootDto> offers) {
        this.offers = offers;
    }
}
