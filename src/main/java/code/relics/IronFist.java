package code.relics;

import static code.ModFile.makeID;

import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import code.util.Wiz;

public class IronFist extends AbstractEasyRelic implements OnAfterUseCardRelic {
    public static final String ID = makeID("IronFist");

    public IronFist() {
	super(ID, RelicTier.COMMON, LandingSound.FLAT);
	this.counter = 0;
    }

    @Override
    public void onAfterUseCard(AbstractCard abstractCard, UseCardAction useCardAction) {

	if (abstractCard.type == AbstractCard.CardType.ATTACK) {
	    this.flash();
	    this.counter++;
	}

	if (this.counter == 2) {
	    this.pulse = true;
	}

	if (this.counter == 3) {
	    this.counter = 0;
	    this.pulse = false;
	    this.flash();

	    Wiz.atb(new ApplyPowerAction(Wiz.adp(), Wiz.adp(), new StrengthPower(Wiz.adp(), 1), 1));
	    Wiz.atb(new ApplyPowerAction(Wiz.adp(), Wiz.adp(), new LoseStrengthPower(Wiz.adp(), 1), 1));
	}

	isDone = true;
    }
}
