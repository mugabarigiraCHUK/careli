/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "entity_change")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EntityChange.findAll", query = "SELECT e FROM EntityChange e"),
    @NamedQuery(name = "EntityChange.findById", query = "SELECT e FROM EntityChange e WHERE e.id = :id"),
    @NamedQuery(name = "EntityChange.findByEntityId", query = "SELECT e FROM EntityChange e WHERE e.entityId = :entityId"),
    @NamedQuery(name = "EntityChange.findByField", query = "SELECT e FROM EntityChange e WHERE e.field = :field"),
    @NamedQuery(name = "EntityChange.findByChangeDate", query = "SELECT e FROM EntityChange e WHERE e.changeDate = :changeDate"),
    @NamedQuery(name = "EntityChange.findByEntityType", query = "SELECT e FROM EntityChange e WHERE e.entityType = :entityType")})
public class EntityChange implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "entity_id")
    private Integer entityId;
    @Lob
    @Size(max = 65535)
    @Column(name = "previous_value", length = 65535)
    private String previousValue;
    @Lob
    @Size(max = 65535)
    @Column(name = "current_value", length = 65535)
    private String currentValue;
    @Size(max = 45)
    @Column(name = "field", length = 45)
    private String field;
    @Column(name = "change_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changeDate;
    @Lob
    @Size(max = 65535)
    @Column(name = "comment", length = 65535)
    private String comment;
    @Size(max = 45)
    @Column(name = "entity_type", length = 45)
    private String entityType;

    public EntityChange() {
    }

    public EntityChange(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EntityChange)) {
            return false;
        }
        EntityChange other = (EntityChange) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "do_.com.bpd.business.referrals.persistence.entities.EntityChange[ id=" + id + " ]";
    }
    
}
