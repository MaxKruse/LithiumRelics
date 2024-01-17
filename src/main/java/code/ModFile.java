package code;

import java.nio.charset.StandardCharsets;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import code.relics.AbstractEasyRelic;

@SuppressWarnings({ "unused", "WeakerAccess" })
@SpireInitializer
public class ModFile implements EditRelicsSubscriber, EditStringsSubscriber, EditKeywordsSubscriber {

    public static final String modID = "lithiumRelics"; // TODO: Change this.

    public static String makeID(String idText) {
	return modID + ":" + idText;
    }

    public static Settings.GameLanguage[] SupportedLanguages = { Settings.GameLanguage.ENG, };

    private String getLangString() {
	for (Settings.GameLanguage lang : SupportedLanguages) {
	    if (lang.equals(Settings.language)) {
		return Settings.language.name().toLowerCase();
	    }
	}
	return "eng";
    }

    public ModFile() {
	BaseMod.subscribe(this);
    }

    public static String makePath(String resourcePath) {
	return modID + "Resources/" + resourcePath;
    }

    public static String makeImagePath(String resourcePath) {
	return modID + "Resources/images/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
	return modID + "Resources/images/relics/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
	return modID + "Resources/images/powers/" + resourcePath;
    }

    public static String makeCharacterPath(String resourcePath) {
	return modID + "Resources/images/char/" + resourcePath;
    }

    public static String makeCardPath(String resourcePath) {
	return modID + "Resources/images/cards/" + resourcePath;
    }

    public static void initialize() {
	ModFile thismod = new ModFile();
    }

    @Override
    public void receiveEditRelics() {
	new AutoAdd(modID).packageFilter(AbstractEasyRelic.class).any(AbstractEasyRelic.class, (info, relic) -> {
	    if (relic.color == null) {
		BaseMod.addRelic(relic, RelicType.SHARED);
	    } else {
		BaseMod.addRelicToCustomPool(relic, relic.color);
	    }
	    if (!info.seen) {
		UnlockTracker.markRelicAsSeen(relic.relicId);
	    }
	});
    }

    @Override
    public void receiveEditStrings() {
	BaseMod.loadCustomStringsFile(RelicStrings.class,
		modID + "Resources/localization/" + getLangString() + "/Relicstrings.json");
    }

    @Override
    public void receiveEditKeywords() {
	Gson gson = new Gson();
	String json = Gdx.files.internal(modID + "Resources/localization/" + getLangString() + "/Keywordstrings.json")
		.readString(String.valueOf(StandardCharsets.UTF_8));
	com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json,
		com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

	if (keywords != null) {
	    for (Keyword keyword : keywords) {
		BaseMod.addKeyword(modID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
	    }
	}
    }
}
