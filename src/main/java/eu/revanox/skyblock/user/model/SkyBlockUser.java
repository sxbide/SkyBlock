package eu.revanox.skyblock.user.model;

import eu.koboo.en2do.repository.entity.Id;
import eu.revanox.skyblock.SkyBlockPlugin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SkyBlockUser {

    @Id
    UUID uniqueId;

    double balance;

    public void setBalance(double amount) {
        this.balance = amount;
        SkyBlockPlugin.instance().getUserManager().saveUser(this);
    }

    public void addBalance(double amount) {
        this.balance += amount;
        SkyBlockPlugin.instance().getUserManager().saveUser(this);
    }

    public void removeBalance(double amount) {
        this.balance -= amount;
        SkyBlockPlugin.instance().getUserManager().saveUser(this);
    }

}
