"""Finish the app's content pack (run after tools/tts/build_content.py): add the module categories from
categories.py, the card animations from scenes.py and the quizzes from quiz_choices.py.

    python3 tools/cards/finish_pack.py [path/to/cards.json]     (default: the app's assets, edited in place)

Categories: every path used by a card must be in exactly one category.

For each item it finds the cue word in the spoken text of its clip and stores how far into the clip it is
(`frac`, a little early so the item lands with the word). Labels written without harakat are copied, vowelled,
from the card's own text. Fails on a missing cue, an unknown clip, items out of time order, or text it
cannot vowel; prints a note when a hand-vowelled word differs from the same word in the card.
"""
import json, os, re, sys

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
sys.path.insert(0, os.path.join(HERE, "..", "tts"))
from scenes import SCENES                      # noqa: E402
from categories import CATEGORIES              # noqa: E402
from shelf import SHELF                        # noqa: E402
from quiz_choices import QUIZ                  # noqa: E402
from build_content import spoken, letters      # noqa: E402
from variety import codex_errors, variety_report, STRICT   # noqa: E402

MARKS = "ًٌٍَُِّْٰ"
HARAKA = re.compile(f"[{MARKS}]")
LEAD = 0.03          # show each item slightly before its word
TYPES = {"steps", "sides", "chain", "timeline", "circles", "meter", "transform", "moon", "stars", "art"}
# illustrated scenes: the beats each one needs (kind: how many), or for shared templates the kinds it knows
ART = {
    "key": {"key": 1, "tooth": 7, "open": 1},
    "rope": {"rope": 3, "cut": 1, "hold": 1, "meaning": 1},
    "ship": {"lord": 1, "ease": 1, "storm": 1, "later": 1, "storm2": 1},
}
ART_KINDS = {
    "lantern": {"lamp", "gust", "glass", "dome", "grow", "dim", "ember", "out", "shatter", "say", "gaze", "hidden",
                "return", "charm", "bind", "untie", "many", "bond", "sever"},
    "trail": {"origin", "up", "lamp", "mark", "good", "bad", "follow", "follow2", "hole", "stray", "idols", "keep",
              "omen", "turn", "word", "goal", "lands", "open", "stuck", "walk", "show", "door", "swap", "light"},
    "ridge": {"stone", "pit", "lean", "walk"},
    "tree": {"root", "trunk", "crown", "fruit", "storm", "hold", "calm", "shed", "bad", "wail"},
    "streams": {"basin", "stream", "divert", "curse", "block", "near"},
    "building": {"base", "pillars", "course", "roof", "glow", "light", "house", "rival", "fall", "crack", "lock",
                 "lower", "wall", "name", "raise", "word", "level", "palace", "brick", "siraj"},
    "gate": {"trumpet", "rise", "lights", "filter", "scrolls", "scale", "bridge", "pass", "trait", "gate", "seal",
             "shut", "open", "noprice"},
    "shrine": {"lamp", "excess", "names", "out", "stone", "idol", "forget", "dome", "feast", "tree", "rock", "shirk",
               "steps", "copy", "takbir", "ask", "garden", "pit"},
    "hourglass": {"glass", "gold"},
    "night": {"book", "cloud", "nights", "towers", "roll", "shrink", "note", "layers", "quake", "calm", "truth", "nocall",
              "adorn", "shoot", "guide", "credit", "rain", "descend", "recite", "keep", "write", "will", "create",
              "trumpet", "ring", "name", "reject", "smoke", "sunwest", "sink", "fire", "journey", "ascend", "fifty"},
    "grain": {"grain", "challenge", "breath"},
    "vessels": {"cup", "equal", "first", "drain", "seal"},
    "cave": {"cave", "sun", "follow", "false"},
    "chart": {"axis", "gain", "loss", "floor", "sins", "cross", "seed"},
    "circuit": {"power", "breaker", "trip", "guard"},
    "verdict": {"walker", "forced", "void", "less", "will", "echo"},
    "board": {"core", "pin"},
    "watch": {"fog", "shield", "wisp", "close"},
    "crowd": {"march", "shout", "arrive", "quiet"},
    "deeds": {"pray", "eyes", "khafi", "returned", "left"},
    "camp": {"camp", "kufr", "joke", "tabuk"},
    "gifts": {"other", "self", "deny", "thank"},
    "amulet": {"hang", "snip", "page", "pure"},
    "knots": {"stones", "pair", "knotted", "fake", "cure"},
    "lights": {"same", "unlike", "lit", "burn"},
    "ranks": {"rank", "veil", "reward"},
    "pillars": {"node", "branch"},
    "neon": {"ijma", "banned", "slaves", "allowed"},
    "oath": {"swear", "pardon", "topple", "plea", "tasbih"},
    "kin": {"self", "foe", "kin", "love", "hate"},
    "ink": {"inscribe", "lean", "base"},
    "lens": {"sheet", "lens", "match", "add"},
    "zellige": {"liken", "fit", "freehand", "extend"},
    "study": {"doubt", "tread", "guess", "derive"},
    "canyon": {"carve", "trapped", "bury"},
    "map": {"spiral", "old", "caliphs"},
    "books": {"pile", "single", "stack"},
    "caravan": {"slip", "around", "defy"},
    # round 1 of the redraw
    "athar": {"dawnlight", "prints", "sidepath", "eras"}, "delta": {"oneriver", "fork", "fray", "onekept"},
    "loom": {"knots72", "caliphs4", "ebb"}, "sabil": {"spout", "pipe", "drink"}, "shore": {"horizon", "scoop", "yield"},
    "ruler": {"sortout", "trace", "veer"}, "mosque": {"differ", "clasp", "behind", "leave"},
    "maze": {"coolwell", "climb", "regret"}, "field": {"sow", "harvest", "winnow"},
    "street": {"against", "mend", "lonewindow"},
    # round 2
    "atelier": {"chisel", "steal", "unheard", "daughter", "nothing"}, "heavens": {"heights", "tremble", "stillness", "relay", "callup"},
    "chamber": {"bedside", "call", "sway", "verse", "padlock", "guideway"}, "scroll": {"towers", "furl", "crumble", "ruins"},
    # round 3
    "majlis": {"quarrel", "tawqif", "flint", "refer", "settle"}, "lane": {"knock", "teach", "debater", "tug"},
    "hilltop": {"mindfog", "doubtspark", "revealed", "creation"}, "wayside": {"sinner", "unharmed", "zealot", "getup", "sound"},
    # round 4
    "flock": {"fold", "unloop", "astray", "gather", "mercy"}, "deck": {"heave", "badorder", "refuse", "carryon", "pledge"},
    "dam": {"picks", "repent", "incline", "breach", "flood"}, "aerial": {"rows", "scatter", "regroup"},
    "souk": {"cheat", "three", "sheathe", "swords"}, "bridge": {"warn", "both", "silent", "unravel"},
    "refuge": {"strife", "stayin", "banners", "melee", "shutters", "seated"},
    # round 5
    "sapling": {"rooted", "wilt", "cloche", "axe", "paint"}, "return": {"opendoor", "turnquit", "regretresolve", "westsun", "doorshut"},
    # round 6
    "shelfshop": {"shelves", "swaptags", "obeyfollow", "dais", "explained"}, "lawh": {"oath", "wawa", "thumma", "lawla"},
    "plaza": {"gust", "greet", "resolve", "askjanna", "give"}, "stall": {"oathsell", "dust", "alone", "fortress", "keepseal"},
    "tent": {"praise", "reply", "whisper", "station"},
    "astrolabe": {"ringgeneral", "ringcovenant", "ringlife", "ringyear", "ringday"},
    "stage": {"marionette", "jitter", "selfmade", "sower", "praised"},
    "eden": {"blame", "decree", "repented", "twoways", "bothways"},
    "casket": {"pry", "sealed", "submit", "followsaid", "hush"},
    "threads": {"pedestal", "putfirst", "beloved", "family"},
    "carpet": {"backside", "despair", "turnover", "hamd"},
    "bird": {"wingfear", "winghope", "headlove", "loveonly", "fearonly", "hopeonly", "soar"},
    "globe": {"pureheart", "earthful", "forgiven", "unmixed", "safeway"},
    "plain": {"refine", "straightin", "noruqya", "nocautery", "noomen", "rely"},
    "river": {"torrent", "holdidol", "holdbanner", "holdcrown", "letgo", "firmhold", "twowords"},
    "milla": {"outmilla", "inmilla", "belie", "conceal", "haughty", "twoface"},
    "booth": {"fortune", "cutoff", "askonly", "unaccepted"},
    "omenroad": {"crowfly", "turnhome", "goodword", "liked"},
    "pitroad": {"road10", "pitfall", "beware"},
    "mankind": {"created", "worshiponly", "sent", "obeyed", "disobeyed"},
    "obey4": {"guide4", "obeycmd", "believe", "avoid", "asshown"},
    "triptych": {"rububiyya", "rubopp", "uluhiyya", "ulopp", "asma", "asmopp"},
    # the books section: «بصائر»، الجزء الأول
    "locks": {"volumes", "arch", "tenlocks", "eleventh", "masterkey"},
    "roads": {"tworoads", "wird", "tafsir", "nextyear", "calmheart", "race", "sincere", "steps3", "satchel"},
    "fork": {"deny5", "atheist", "deist", "agnostic", "indifferent", "allfour"},
    "centuries": {"ages", "twist", "trustlost", "thinkers", "newcoat"},
    "cosmos": {"monolith", "nothingness", "bang", "fromnothing", "itself", "hascreator", "bubbles", "bubblestart", "dice",
               "nodice", "twoboxes", "notempty", "energyfloor", "pairs", "sealedbox", "insidebox", "zerosum", "cooling", "deadborn"},
    "billiard": {"formula", "noclock", "emptytable", "balltrace", "noproduce"},
    "dominoes": {"domino", "blurorder", "fuzzy", "steady", "civ", "whomade", "regress", "soldiers", "nobullet"},
    "panels": {"frames", "made", "orchid", "care"},
    "forge": {"thrift", "vast", "forgeiron"},
    "scales": {"tiny", "balance", "trust"},
    "desert": {"planets", "forest", "sandchip", "letters"},
    "cell": {"helix", "readout", "nomatter", "whence", "livecell", "inject", "insideonly", "notmade"},
    "planks": {"chasm", "gapsbridge", "solid", "infoblocks"},
    "lab": {"waitdoor", "labfail", "prize"},
    "screen": {"scramble", "target", "designer"},
    "kunafa": {"eyerow", "meteor", "rainmix", "lavabake", "baker"},
    "sieve": {"quotecard", "loop", "othercause"},
    "kitchen": {"pool", "pantry", "twotrays", "beaks"},
    "karyotype": {"counts", "fuse", "potato", "lineage"},
    "anatomy": {"list180", "shrinklist", "glands", "graph"},
    # round 14: «الصفات واليوم الآخر»
    "heights": {"above", "withall", "notmixed", "moonwalk"},
    "revelation": {"sendown", "reciters", "memorize", "mushaf"},
    "gaze": {"lookup", "veiled", "infer"},
    "resurrection": {"horn", "arise", "scrollsfly", "weigh", "sirat"},
    "nameswall": {"husna", "idolnames", "tashbih", "emptied"},
    "register": {"fromtext", "byopinion", "qadim", "awwal"},
    "unseen": {"known", "sealdoor", "denyknown", "claim", "onlyallah"},
    "tensigns": {"tenring", "sixsigns", "sinkings", "yemenfire"},
    "barzakh": {"twoangels", "threeq", "answers", "rawda", "hufra"},
    # round 15: «الشرك ووسائله»
    "fourrules": {"rulebook", "ruleone", "ruletwo", "rulethree", "rulefour"},
    "anwat": {"lotetree", "sunanpath", "askanwat", "takbirsunan"},
    "offering": {"twopaths", "cursed", "vowkeep", "vowbreak"},
    "storm": {"duasea", "refugeonly", "shrinecall", "livinghelp"},
    "twoseals": {"sealedgate", "permit", "pleased", "refused", "ikhlasopen"},
    "plaingrave": {"nodome", "nofeast", "noidol"},
    "footsteps": {"oldprints", "featherpair", "joinpagans", "bowidols", "firmgroup"},
    # round 16: «أركان الإيمان»
    "sixroots": {"threeroots", "threemore", "branchout"},
    "courts": {"enter", "court5", "court6", "court1"},
    "verseguard": {"affirmverse", "nobend", "nohow"},
    "heartjar": {"fillup", "sinkdrop", "stillfaith", "lawfulcrack", "mustard"},
    "decree": {"allknown", "written", "willed", "allcreated"},
    "wholeman": {"threeparts", "nodeeds", "tongueonly", "heartonly"},
    "heartlights": {"samelight", "unequal", "brightthree", "dimtwo", "burnaway"},
    "threelives": {"ranks", "jibril", "mikail", "israfil", "scribes"},
    # round 17: «معرفة النبي ﷺ»
    "twolands": {"crossover", "neverclosed", "excused"},
    "proofscroll": {"refusegold", "trusted", "fewtomany", "reproach", "saferider"},
    "lastbrick": {"sealofall", "liars", "palacegap", "lastone", "toall"},
    "nightjourney": {"nightride", "ledprayer", "sevenheavens", "lotetop", "fiftytofive"},
    # round 18: «منهج أهل السنة»
    "allegiance": {"believer", "nobond", "evenkin", "loveandhate"},
    "companions": {"fourfirst", "theten", "allcompanions", "holdtongue", "tworewards", "bestgens"},
    "jamaa": {"bidgood", "keeprows", "withleaders", "onewall"},
    "household": {"household", "mothers", "notlahab", "noexcess", "nosaving"},
    # round 19: «السنة والبدعة»
    "twowings": {"sideroad", "twowings", "nosincere", "nofollow"},
    # «بصائر»، الجزء الثاني (batch 7)
    "dawnfear": {"fearstorm", "fourq", "sameday", "grandeur", "ownsource"},
    "spring": {"spring", "muddy", "witness", "fromabove"},
    "elephant": {"blindsix", "parts", "pullout", "overview"},
    "detector": {"embers", "cage", "cageopen", "plate", "plateglow"},
    "safa": {"twopoles", "safahill", "witnessed", "samethread"},
    "mistpath": {"impossible", "wonder", "align", "sunsea"},
    "tawatur": {"sparks", "converge", "pot", "ringclose"},
    "brethren": {"twobranch", "ishmaellight", "likemusa", "falsefade", "fulfilled"},
    "threepeaks": {"sinai", "seir", "paran", "names3", "tin"},
    "kedar": {"servant", "quietstreet", "eyesopen", "kedarhomes", "selasong"},
    "openhouse": {"flocks", "dayandnight", "welling", "pilgrims"},
    "vineyard": {"vineyard", "newkeepers", "rejected", "cornerset", "lastlit"},
    "statue": {"statue", "handless", "fillearth", "kingdoms"},
    "sceptre": {"sceptre", "handover", "linedark", "unshackle"},
    "awaited": {"yathrib", "cell", "throne", "twelve"},
    "overseer": {"overbook", "strikeone", "striketwo", "strikethree", "ezra"},
    "myths": {"manyverses", "radiator", "sevenhorses", "clean"},
    "gates": {"beacon", "gateone", "gatetwo", "gatethree"},
    "nebula": {"primalsea", "dukhan", "condense", "fabric", "widen"},
    "earthmoon": {"breathe", "moonfade", "innerwave", "waterlife"},
    "twowaters": {"curdle", "mixing", "extract", "stages"},
    "spine": {"coccyx", "streak", "sinks", "joints"},
    "nightglobe": {"openquran", "onelight", "everyhouse", "daily"},
    "listener": {"offers", "fussilat", "returns", "heartrise"},
    "challenge": {"dare", "spears", "pearls", "depth"},
    "lifeline": {"dense", "links", "topofhundred"},
    "darkvale": {"darkvale", "swapstone", "daybreak", "rights"},
    "foretold": {"secret", "sixmonths", "fleet", "hira", "martyrs"},
    "siffin": {"twobricks", "twocamps", "alarm", "certain"},
    "conquests": {"quds", "threelands", "constantinople", "aman"},
    "skyline": {"shepherds", "skyward", "markets", "foam"},
    "greenarabia": {"greening", "buriedrivers", "goldpillars", "belowzero"},
    "fogstones": {"twoages", "fewfog", "threeorigins", "cellglow"},
    "sunproof": {"askingsky", "heartsun", "seekpath", "noexam", "threedoors"},
    "postman": {"pillarhawa", "dimheart", "doubtsfall", "twodoors"},
    "acidbase": {"shouldnot", "reaction", "braingears", "threepillars", "innocentwall"},
    "clinic": {"threefogs", "diploma", "prescription", "proofsealed", "deadleaf"},
    "popcorn": {"popthoughts", "oneq", "hydra", "fuelfire", "sarih"},
    "madroad": {"seekrefuge", "roadshout", "morebubbles", "ignorefade", "mirage"},
    "trialroad": {"trialworld", "fishdry", "freechoice", "syringe", "threadlong"},
    "thermo": {"threethermo", "sumthermo", "mediapile", "dividedblessings", "needdua"},
    "fourgates": {"fourways", "foxhole", "greatestway", "readybefore"},
    "examtrain": {"amanah", "nomodel", "trainride", "threequestions"},
    "lifeguard": {"doctorfree", "rescue", "forus", "fruitprayer"},
    "howwhy": {"orderlyworld", "howtools", "whysilent", "onlygod", "jabirflask"},
    "twopaths": {"citadel", "fairscales", "forkroads", "addict", "stepmore"},
    "teacherdesk": {"cutstrings", "teacher", "forward", "messengers", "willinside"},
    "steadyrevelation": {"seizurewave", "blank", "verseorder", "onmount", "eclipse"},
    "isolatedbox": {"breaknorm", "glassbox", "notisolated", "qudsraised", "latescroll"},
    "stormcause": {"cloudangel", "whychain", "mechanism", "boilpot", "wingsfaith"},
    "datedscrolls": {"claimscroll", "timeline629", "meccankahf", "farchurch"},
    "tworams": {"eastwest", "idolhorns", "twohorns", "danielram", "opendoors"},
    "dotsvoice": {"movedots", "amateur", "voicechain", "rootqara"},
    "palimpsest": {"mosquefind", "erasedpage", "inhearts", "isnadchain"},
    "sevenstreams": {"sevenbranches", "twocompanions", "bothright", "choosebranches", "dottedpapyrus"},
    "stairsteps": {"ransomram", "graduated", "stilltrue", "willthird", "fewnine"},
    "sunbow": {"sunorbit", "ceilingall", "allbow", "behindhill"},
    "caveboulder": {"boulder", "delayed", "centuryanswer", "tatarmosques"},
    "wagesroom": {"serveother", "failgrade", "dustworks"},
    "justscale": {"exactscale", "mustardgrain", "praiseheart", "messengersfirst"},
    "templesands": {"statues", "nostatue", "steles", "medinamarks"},
    "twoladders": {"materialscale", "civculture", "longestciv", "qarawiyyin", "uhudlesson"},
    "bridgegap": {"scrollaway", "onerakah", "sunnabridge", "explains", "qiblaturn"},
    "scrollrack": {"claim200", "sahifa", "twocopies", "fiftytwo", "twentythousand"},
    "chainlinks": {"everyletter", "fiveconditions", "missinglink", "travel"},
    "onemessenger": {"classify", "eyewitness", "qubaturn", "envoys", "muezzin"},
    "cloakfull": {"closest", "cloak", "marwantest", "eighthundred"},
    "hundredswap": {"olderbooks", "swapped", "restored", "witnessgate"},
    "courtgate": {"exemptions", "turnaway", "safewall", "fourwitness", "coverveil"},
    "emptyhouse": {"leftlittle", "bilaldebt", "fourcamels", "goldaway"},
    "lamphouses": {"onlyone", "wisdoms", "rooms"},
    "threeparts": {"quarters", "jahilidark", "lifestages", "ownpurse", "shaqaiq"},
    "shieldtent": {"standingfor", "shieldspend", "ownbox", "consent", "harmony"},
    "ledgerscale": {"tradeledger", "cradle", "obligations", "roadsign"},
    "fairhouses": {"bondvs", "justicecond", "tilted", "equalnights", "khadijaonly"},
    "truemeasure": {"courtfall", "samebook", "truthsame", "selfsaw", "aboveref"},
    "brainset": {"executor", "penfield", "filterpaper", "tvset", "spirit"},
    "falsewaves": {"tunecosmos", "noask", "threecures", "springmass", "plusminus"},
    "whymuslim": {"innate", "manyproofs", "onlydoor", "palmtrunk", "springwater", "rebuilt"},
    "surrender": {"prophetsrow", "twoconds", "fourrakah", "stonesbow", "sharedship", "khatma"},
    "parentsgate": {"bestdeeds", "mother3", "rughim", "fihima", "midgate"},
    "kinthreads": {"hangs", "widens", "reconnect", "twofold"},
    "neighbors": {"counsel", "harm", "nearest", "broth", "beam"},
    "orphangarden": {"together", "threeranks", "weakroots", "twodaughters", "quietpoor"},
    "homeledger": {"bestrank", "sharetable", "weighflaw", "homelight", "firstdinar"},
    "sixrights": {"sixfirst", "sixrest", "onebody", "building", "restrain"},
    "sickroom": {"visit", "orchard", "threeacts", "angels"},
    "reconcile": {"whispers", "justice", "mendword", "village", "debt"},
    "heavyscale": {"heaviest", "twokeys", "threetraits", "fastrank", "twohouses"},
    "gentlerain": {"twotraits", "rainvsflood", "bucket", "signposts", "zanahu"},
    "pardonbook": {"threenever", "strongman", "mountains"},
    "townsquare": {"speck", "beautyok", "levelout", "childrenlead", "sinkdown"},
    "truthroad": {"toberr", "tofujur", "fourcalls", "market", "bedrank"},
    "mercyrain": {"kiss", "mirrorrain", "babycry", "stoneheart"},
    "guestnight": {"basinfill", "twolights", "nevernot", "lampout", "onecloth", "shoulder"},
    "brightface": {"smallgood", "halfdate", "drawnear", "listen", "goodnews"},
    "tonguegate": {"safering", "twolocks", "fallword", "riseword", "organs"},
    "backbite": {"absent", "twoverdicts", "coal", "inkdrop", "shieldback"},
    "rumorchain": {"sieve", "falsedream", "passedon", "fourmarks", "twograins"},
    "cursereturn": {"fournot", "boomerang", "shutgates", "freecamel", "noaid"},
    "turnback": {"backs", "greetfirst", "records", "sowing"},
    "salamstreet": {"lovechain", "plateall", "whofirst", "tens", "quietnight"},
    "threeknocks": {"threetimes", "aside", "askin", "sayname", "grownchild"},
    "sharedtray": {"yourside", "latename", "noblame", "threebreaths", "rightward", "nosupper"},
    "majlisring": {"makeroom", "atedge", "clearing", "threesit", "remembered"},
    "wakedawn": {"wakeup", "sleepsister", "nightwake", "lastten"},
    "newrobe": {"comesdown", "threegarments", "wornrenewed", "curtain", "rightsleeve"},
    "eightgates": {"inout", "namedwater", "eightdoors", "twopure"},
    "threewords": {"threewords", "threegifts", "homegreet", "pitfalls"},
    "rightfoot": {"rightin", "leftout", "fourlights", "keptsafe", "manylights"},
    "afterprayer": {"salamturn", "hundred", "nobarrier", "threeasks", "nojadd"},
    "twodoors": {"likesurah", "tworakah", "consulted", "gooddoor", "closedoor"},
    "morningevening": {"bookends", "dawnguard", "threeshields", "mulk", "tillsunrise"},
    "covenant": {"fourparts", "dayornight", "abu", "fivesteps", "dayshukr"},
    "noharm": {"bounce", "scorpion", "threecontent", "heldup", "sevenrings"},
    "bedtime": {"threesurahs", "watcher", "twoverses", "rightside", "tasbihcols"},
    "nightdoors": {"callin", "shutdoors", "coverjars", "lastpage", "lampfire"},
    "springheart": {"cloudlift", "bowedtags", "springheart", "eightpairs", "endless"},
    "deepsea": {"praisestars", "boatheld", "dhunnun", "onename", "shore"},
    "campguard": {"turnsback", "coolfire", "suffice", "trenchwind", "neighbour"},
    "roughground": {"smoothed", "nolaw", "strongweak", "whispercut"},
    "mountaindebt": {"comestoali", "mountainpaid", "heavyload", "workandask", "repaid"},
    "betterjar": {"brokenjar", "ummsalama", "takengiven", "visitgraves"},
    "healingbed": {"nobas", "sevenvisits", "handonpain", "lastword"},
    "rainwind": {"usefulrain", "notthestar", "strongwind", "rainaround", "thunderhush"},
    "journey": {"mounted", "piety", "homecoming", "keptboth", "updown"},
    "iftar": {"guestdua", "rootsdrink", "angelsover", "calmfast", "firstdates"},
    "sneezebook": {"threeanswers", "nopraise", "coolword", "twopraises", "blessit"},
    "weddinglights": {"tworings", "hasanayn", "fatherofprophets", "venomback", "thanksgift"},
    "alivetree": {"twotrees", "outweighs", "moist", "tenletters"},
    "lightheavy": {"featherwords", "seafoam", "fourwords", "kanz", "thousandgood"},
    "tenfold": {"tenback", "misertag", "fromeverywhere", "nearestmost", "abrahamic"},
    "seventyhundred": {"seventyplus", "turnall", "washpray", "tallyhundred", "lastthird"},
    "raisedhands": {"duaworship", "nohaste", "forbrother", "twogoods", "nearestsujud"},
    "intentlamp": {"openwith", "twopillarsof", "innerouter", "samewash", "hijraroads"},
    "questioner": {"fourquestions", "nestedranks", "pairsplit", "tworungs", "fivelocked", "toweringup"},
    "fivepillars": {"fivebuild", "basefirst", "saidanddone", "twoservitudes", "orderofduty"},
    "fourwrit": {"threestages", "fourwrit", "endings", "nesteddecree"},
    "radd": {"bounceback", "twogatesmeet", "bothreturned", "twomothers"},
    "himaedge": {"threezones", "stepclear", "nearhima", "threeshepherds", "slopedown", "limbsfollow"},
    "fivecounsel": {"thriceword", "cleanfull", "fiveto", "bookfirst", "measuringline", "pointgood"},
    "threeshown": {"threelit", "sealedfast", "bloodwealth", "sevenconds", "iqal", "outerjudged"},
    "canbear": {"shutandclimb", "sitpray", "manyquestions", "partwater"},
    "goodonly": {"pureonly", "eatandwork", "blockeddua", "fourcauses", "noghulul"},
    "doubtfree": {"rayhanatan", "murkyclear", "widermargin", "steadyjitter"},
    "notmyconcern": {"betterless", "passby", "enjoinstill", "twobaskets", "chatterdoor"},
    "likeforbrother": {"anasyears", "baseremains", "mirrorgood", "faiththread", "warmnet"},
    "inviolable": {"threesacred", "tonguehearthand", "farewellday", "partmissing"},
    "goodorsilent": {"speakorstill", "rightscount", "guestwelcome", "neighborcounsel", "threehostdays"},
    "angerember": {"repeatnoanger", "pulledoff", "emberquench", "sitthenlie", "smokeandwater", "trulystrong"},
    "ihsaneverything": {"twowritings", "sharpenrest", "bluntno", "outweighharm", "reachesbeast"},
    "taqwaeverywhere": {"muadhabudharr", "shieldsecret", "patchtear", "tworanks", "fourmanners"},
    "guardhim": {"ridebehind", "keepbounds", "crowdcannot", "ownwhip", "easethenhard", "missandhit"},
    "modesty": {"oldprophecy", "sternword", "hayagauge", "pullsback", "placebadr", "faithbranch"},
    "straightpath": {"sufyanasks", "fouralong", "safeend", "bothsides"},
    "intojanna": {"fourticks", "heldbelief", "mercygate", "notlisted", "neverdrops"},
    "halfofiman": {"halffilled", "praisefills", "lightproofglow", "patiencethree", "proofforagainst"},
    "noinjustice": {"forbadeself", "wrongplace", "fourneeds", "curedone", "nothingadded"},
    "notonlywealth": {"richpoor", "dhikrcoins", "spreadsgood", "selfcharity", "maruftiers"},
    "everyjoint": {"jointslight", "dutyorextra", "fivesadaqa", "duhatwo", "clearroad", "eachsunrise"},
    "askyourheart": {"birrring", "hiddenknot", "consultheart", "answeredfirst", "revealedlit"},
    "holdfast": {"movingsermon", "linkedrings", "onelitway", "tightgrip", "notbeforeus"},
    "headofmatter": {"greatyeteasy", "tentparts", "shieldandwater", "polefalls", "sealtongue"},
    "fourrulings": {"keepduties", "boundsfences", "silentmercy", "fourquads", "knowingsilence"},
    "zuhdlove": {"twoloves", "stepaside", "handvsheart", "zuhdrungs", "noshunhalal", "poetfour"},
    "harmblocked": {"harmwall", "twoharms", "fourchapters", "openpasture", "noharmneighbor", "threadsstrong"},
    "proofonclaimant": {"bareclaims", "proofoath", "twoaimsqada", "halfwaymeet", "halfdebt", "bothcontent"},
    "changeevil": {"darkblots", "handtongueheart", "dimbutlit", "faithgauge", "barrierexcused"},
    "nojealousy": {"twelvephrases", "envysmoke", "lightmytoo", "firstenvy", "backturned", "fournots"},
    "relievegrief": {"liftweight", "smallvsbig", "helpedhelper", "givetime", "cloaked", "knowledgeroad"},
    "recordbook": {"twocolumns", "multiply", "badleft", "camelseven", "mercywins"},
    "awliya": {"upright", "twotiersawliya", "fardthennafl", "guidedsteps", "answereddua"},
    "pardoned": {"threeforgiven", "accidentspill", "forcedcalmheart", "answeredbaqara", "stillowes"},
    "stranger": {"shoulderword", "takesonly", "dontwait", "storeahead"},
    "followsrevealed": {"needlealigns", "lovesdoes", "smallandlarge", "datestone"},
    "skyhighforgive": {"threecauses", "cloudstosky", "earthfulpardon", "elixirdrop", "firstlastpage"},
    "moonface": {"neithertall", "moonnotsword", "brighterthanmoon", "silvercast", "lastwhosaw", "gradedmarks"},
    "garments": {"shirtstripes", "whitecloths", "perfumekept", "namednew", "notreturned3"},
    "humbletable": {"norecline", "tablelifted", "basmalabless", "firstandlast", "threesips", "milkbless"},
    "namesring": {"threenames", "lastnames", "yearsline", "mercynames", "atforty"},
    "simpleliving": {"coldhearth", "barleynights", "coolshade", "milkfromneighbors", "chaffblown"},
    "clearspeech": {"spacedwords", "threetimesword", "warmsmile", "neverbarred", "aliriding"},
    "truejest": {"truejoke", "littlebird", "everycamel", "twoears", "notcheap", "youngagain"},
    "gratefulservant": {"thankfulnight", "threesuras", "longstanding", "nightplan", "heartawake", "steadydrops"},
    "recitelight": {"drawnout", "readtome", "simmering", "loudorlow", "heardatnight"},
    "onlyaservant": {"servantmessenger", "anystreet", "homechores", "smallgift", "plainsaddle", "walkingvisit"},
    "noblemanners": {"neveruff", "shyerthan", "pardonsback", "chooseseasier", "windgiving", "giftreturned"},
    "lastday": {"curtainlift", "noagony", "mondayrest", "zumarverse", "citylitdark"},
    "echoadhan": {"answercall", "radeetu", "twohayya", "highstation", "betweencalls"},
    "hajjpillar": {"fifthpillar", "hastenhajj", "oncelife", "umrahspan"},
    "beforehajj": {"debtledger", "tawbanasuh", "rightsback", "cleanpurse", "goodcompany"},
    "miqatwhite": {"washscent", "twowhites", "labbayk", "keephair", "mountride"},
    "fivemiqat": {"threemiqat", "twomiqat", "passmiqat", "fromhome", "todaynames", "tohill"},
    "threenusuk": {"umrahsteps", "threeroads", "tamattu", "hajjmonths", "ifheld"},
    "childpilgrim": {"validyoung", "guardianlabbayk", "throwsforhim", "carriedround", "guardianreward"},
    "ihramlimits": {"nocutscent", "nosewn", "nohunt", "norafath", "mayshade"},
    "sevenrounds": {"blackstone", "sevencircuits", "behindmaqam", "safamarwa", "umrahdone"},
    "arafatday": {"tarwiya", "arafastand", "muzdalifanight", "mashardawn"},
    "nahrday": {"fourrites", "noharaj", "tashriqthree", "deputythrow"},
    "hadyoffer": {"hadyshare", "pureearning", "tenfast", "arafaopen", "fourdays"},
    "farewellhouse": {"stayworship", "doubledhere", "farewelltawaf", "walkonward", "madinahmosque"},
    "eidoffering": {"adhadef", "twokabsh", "sunnahor", "betterthanprice", "familyall"},
    "fourconditions": {"anam", "agecheck", "fourdefects", "timewindow", "owned"},
    "bestoffering": {"rankkinds", "bestshape", "amlah", "disliked", "fourteen"},
    "whocovers": {"onesheep", "seventh", "notshared", "notmorethan7"},
    "threethirds": {"eatgivegive", "thirds", "nosale", "designated"},
    "dhulhijjanails": {"noclip", "onlyhim", "tawbanokaffara", "likepilgrim", "dhakatconds"},
    "ramadangates": {"jannahopen", "narshut", "chainedsmoke", "forgivenpast", "threespans"},
    "fastsecret": {"beyondcount", "secretheart", "muskscent", "fastshield", "twojoys", "rayyangate"},
    "hilalwatch": {"year2", "sighting", "thirtydays", "onewitness", "monthlength"},
    "whomustfast": {"fiveconds", "childtrain", "feedperday", "newmuslim", "liftedpen"},
    "travelersick": {"travelchoice", "easierway", "sickgauge", "nofaultfound", "medicaldoctor"},
    "excusedays": {"makeupdays", "nursingfear", "beforenext", "rescuer"},
    "fastmanners": {"twotiers", "prayontime", "fourharms", "sleptprayer", "earseyetongue"},
    "suhooriftar": {"predawnmeal", "nearfajr", "sunsethaste", "ratabtamrwater", "thirstgone"},
    "fastbreakers": {"eatdrink", "nutritivedrip", "plainshot", "scentok", "holdandrepay"},
    "forgotfast": {"threerings", "forgotate", "didntknow", "notbreakers", "rememberstop"},
    "lastten": {"lasttennights", "wakefamily", "oddnights", "movingnight", "hiddenseek"},
    "fitrsaa": {"everysoul", "onesaa", "surplus", "beforeeidprayer", "foodnotvalue"},
    "tarawihnight": {"nightrows", "afterisha", "confirmedsunnah", "firstnight", "noadhan"},
    "rakatcount": {"twentyrakat", "elevenrakat", "twobytwo", "imamcare", "heartbest"},
    "withimam": {"firsttolast", "twoimams", "evenitout", "rowsfill"},
    "witrclose": {"singlewitr", "apartjoined", "twowitrforms", "witrsurahs", "subhanmalik"},
    "qunuthands": {"qunutsunnah", "afterbow", "wordsfree", "aminecho", "notoverlong"},
    "itikafstay": {"staymosque", "cutties", "sunnahvow", "lasttenstay", "threemosques"},
    "mosqueonly": {"mosqueandintent", "amoment", "fastoptional", "nothome", "hotelhall", "insidewall"},
    "itikaftimes": {"enterssunset", "entersdawn", "leavesforeid", "vowthree"},
    "leavingmosque": {"outvoid", "needout", "onlyneed", "statedcondition"},
    "seclusionadvice": {"knockdoor", "nocrowd", "phonejail", "silencespeechsight", "notebook"},
    "purewater": {"keyofprayer", "twopurities", "minormajor", "fourwaters", "threechanges"},
    "vesselrules": {"allvessels", "goldsilver", "otherspots", "silverpatch", "goldpatch"},
    "restroomway": {"leftfootin", "rightfootout", "noqibla", "waterorstones"},
    "miswak": {"anytime", "fourmoments", "aracktwig", "fitrafive", "siwakbenefits"},
    "wudusix": {"wuduneeded", "sixfard", "orderflow", "wuduconds", "dryspot"},
    "wudusunnahs": {"rewardnoblame", "startsunnahs", "rightthrice", "onceorthrice", "allthreeways"},
    "wudubreakers": {"wuduglowbreaks", "deepsleep", "doubtstay", "camelmeat"},
    "khuffwipe": {"khuffallowed", "wipeperiod", "wipetop", "clockstarts", "wipeends"},
    "castwipe": {"castonarm", "nolimit", "trimexcess", "needmeasure"},
    "fullwash": {"wholebody", "newmuslimwash", "twoghusl", "perfectsteps"},
    "dryablution": {"noewater", "strikeearth", "waterfound", "liftshadath"},
    "impurities": {"waterremoves", "groundwash", "sevenwash", "sprinkle", "rubwring"},
    "naturalcycle": {"monthrhythm", "seednourish", "nolimits", "usualyears", "easerule"},
    "cyclechanges": {"shiftingdays", "tintedcells", "whitesign", "pregnantrule"},
    "excusedprayer": {"noprayermakeup", "onerakahtime", "dhikrallowed", "readbyheart"},
    "excusedfast": {"fastmakeup", "ritesbutawaf", "purifiedwash", "aftersunset", "farewellwaived"},
    "continuousflow": {"unbrokenline", "knownhabit", "wudueachprayer", "threecases"},
    "afterbirth": {"cradlebegins", "pureearly", "fortymark", "washatforty"},
    "zakatgrowth": {"growsprout", "thirdpillar", "threewisdoms", "bondnet", "twokinds"},
    "zakatconds": {"firstislam", "nisabowned", "fullyear", "fourwealth", "debtzakat"},
    "goldnisab": {"quarterten", "twonisabs", "nocombine", "fiveof200", "jewelry"},
    "tradegoods": {"shopshelf", "threetradeconds", "valueatyearend", "inheritedno", "rentincome"},
    "harvestshare": {"storedgrains", "tenthorhalf", "ripenow", "threehundredsaa", "buriedfifth"},
    "livestockzakat": {"threeherds", "herdnisab", "grazingfree", "mostlyfed", "onesheepdue"},
    "eightrecipients": {"firstfour", "lastfour", "anyclass", "poorneedy", "notdependents"},
    "prayerrank": {"secondpillar", "miraj", "openclose", "ontimeanyway", "sevenyears"},
    "adhancall": {"announcetime", "kifaya", "fifteenphrases", "turnrightleft", "nostepout"},
    "fivetimes": {"zuhrshadow", "asryellow", "maghribisha", "fajrline", "middleprayer"},
    "prayerconds": {"purityandtime", "threemoreconds", "intentheart", "condsvspillars"},
    "walktoprayer": {"smallsteps", "waitdhikr", "straightrows", "rightfootmosque", "widestance"},
    "openingtakbir": {"facequibla", "raisehands", "handsonchest", "farapartsins", "alternate"},
    "recitation": {"fatihakey", "aminloudlow", "midlength", "longshort", "thirdfourth"},
    "bowing": {"flatback", "threetasbih", "riseupsami", "fillheavens", "handsafterrise"},
    "prostration": {"kneesfirst", "sevenlimbs", "nearestbow", "armsapart", "greatestpillar"},
    "betweensujud": {"iftirash", "rabbighfirli", "vertebrae", "followimam", "restsit"},
    "tashahhud": {"indexpoint", "tahiyyat", "salamsides", "fourrefuge", "tawarruk"},
    "pillarsofprayer": {"threetiers", "fourteenpillars", "somepillars", "sittingside", "calmness"},
    "wajibatprayer": {"eightduties", "transitions", "extrareward", "threetoten", "followerpraise"},
    "dislikedacts": {"gazedown", "fidgets", "distractions", "ornatewall", "presentheart"},
    "forgetfulness": {"threeaims", "sahwcauses", "buildonless", "returnifnotup", "alertimam"},
    "postprayerdhikr": {"istighfarthree", "thirtythree", "kursiyshield", "tentimes", "imamturns"},
    "twelverakat": {"twelvearches", "aroundprayers", "travelkeeps", "houseinjannah"},
    "witrnight": {"strongestnafl", "witrwindow", "oneto13", "ninejoined", "lastnightwitnessed"},
    "forenoon": {"duhanafl", "twotoeight", "duhawindow", "threecounsels", "hotsand"},
    "recitationbow": {"versebow", "listenerkinds", "bowsubhan", "sajadawajhi", "fromstanding"},
    "forbiddentimes": {"twodawnbans", "threedaybans", "allowedwithcause", "shadowstops"},
    "congregation": {"rowsduty", "evenwar", "benefits4", "stepreward", "familyneighbors"},
    "wholeads": {"rankorder", "readerfaqih", "hostfirst", "drawlots", "unknownfault"},
    "sickprayer": {"hardshipease", "standsitside", "nodlower", "bentstand", "sickjoin"},
}
TARGET = {"title": "title", "face": "face", "deep": "deeper", "had": "hadith"}

errors, notes = [], []


def err(cid, msg):
    errors.append(f"{cid}: {msg}")


def target_of(key):
    """Audio key -> vowelled-text key: face-0 -> face:0, deep-1 -> deeper:1, title -> title."""
    if key == "title":
        return "title"
    kind, i = key.split("-")
    return f"{TARGET[kind]}:{i}"


def speech_raw(sp, key):
    if key == "title":
        return sp["title"]
    kind, i = key.split("-")
    return sp[TARGET[kind]][int(i)]


def bare(s):
    return HARAKA.sub("", s)


def is_boundary(s, i):
    return i < 0 or i >= len(s) or not ("ء" <= s[i] <= "ي" or s[i] == "ـ")


CLITICS = "وفبلك"


def vowel_from(texts, label):
    """Find `label` (no harakat) in the vowelled texts, the item's own sentence first; return the vowelled span.

    Per text: whole words first, then after a one-letter prefix (و ف ب ل ك) that the label leaves out.
    Only if no text has it that way, any position in any text."""
    want = label.strip()

    def search(v, mode):
        kept, idx = [], []
        for j, ch in enumerate(v):
            if ch not in MARKS:
                kept.append(ch)
                idx.append(j)
        s = "".join(kept)
        start = 0
        while True:
            p = s.find(want, start)
            if p < 0:
                return None
            end = p + len(want)
            if mode == "word":
                good = is_boundary(s, p - 1) and is_boundary(s, end)
            elif mode == "clitic":
                good = p > 0 and s[p - 1] in CLITICS and is_boundary(s, p - 2) and is_boundary(s, end)
            else:
                good = True
            if good:
                a, b = idx[p], idx[end - 1] + 1
                while b < len(v) and v[b] in MARKS:
                    b += 1
                return v[a:b]
            start = p + 1

    for v in texts:
        for mode in ("word", "clitic"):
            r = search(v, mode)
            if r:
                return r
    for v in texts:
        r = search(v, "any")
        if r:
            notes.append(f"vowelled «{label}» from inside a word: «{r}»")
            return r
    return None


def words_with_marks(s):
    return re.findall(f"[ء-يـ{MARKS}]+", s)


def check_vowels(cid, text, vocab):
    """Compare each hand-vowelled word with the same word in the card (ignoring its last letter's case)."""
    for w in words_with_marks(text):
        b = bare(w)
        if len(b) < 2:
            continue
        if not HARAKA.search(w):
            err(cid, f"no harakat on «{w}» in «{text}»")
            continue
        forms = vocab.get(b)
        if not forms:
            continue

        def body(x):                      # drop the marks on the last letter
            return re.sub(f"[{MARKS}]+$", "", x)
        if body(w) not in {body(f) for f in forms}:
            notes.append(f"{cid}: «{w}» — the card has {' / '.join(sorted(forms))}")


def vowelled(cid, s, texts, vocab):
    if not s or not re.search("[ء-ي]", s):          # nothing to vowel (a number written in digits)
        return s
    if HARAKA.search(s):
        check_vowels(cid, s, vocab)
        return s
    v = vowel_from(texts, s)
    if v is None:
        err(cid, f"cannot vowel «{s}»: not in the card text (write it with harakat)")
        return s
    v = v.strip(" ،؛:.")
    check_vowels(cid, v, vocab)
    return v


def build(path):
    pack = json.load(open(path, encoding="utf-8"))
    speech = json.load(open(os.path.join(HERE, "..", "tts", "speech.json"), encoding="utf-8"))
    cards = {c["id"]: c for c in pack["cards"]}
    for cid in SCENES:
        if cid not in cards:
            err(cid, "no such card")
    done = 0
    for c in pack["cards"]:
        spec = SCENES.get(c["id"])
        c.pop("scene", None)
        if spec is None:
            continue
        cid = c["id"]
        keys = [a["key"] for a in c["audio"]]
        diac = c.get("diac", {})
        vocab = {}
        for v in diac.values():
            for w in words_with_marks(v):
                vocab.setdefault(bare(w), set()).add(w)
        for e, pending in codex_errors(cid, spec, lambda at, cid=cid: speech_raw(speech[cid], at) if at in keys else ""):
            (notes.append(f"{cid}: codex, to be redrawn: {e}") if pending else err(cid, e))
        if spec["type"] not in TYPES:
            err(cid, f"unknown scene type {spec['type']}")
        items, last_at, last_pos, last_order = [], None, {}, (-1, -1.0)
        for n, it in enumerate(spec["items"]):
            at = it.get("at") or last_at or "face-0"
            last_at = at
            if at not in keys:
                err(cid, f"item {n}: no clip {at}")
                continue
            heard = letters(spoken(speech_raw(speech[cid], at)))
            frac = 0.0
            if it["cue"]:
                cue = letters(it["cue"])
                p = heard.find(cue, last_pos.get(at, 0))
                if p < 0:
                    err(cid, f"item {n}: cue «{it['cue']}» not heard in {at} after position {last_pos.get(at, 0)}: {heard}")
                    continue
                last_pos[at] = p + 1
                frac = max(0.0, p / max(len(heard), 1) - LEAD)
            order = (keys.index(at), frac)
            if order < last_order:
                err(cid, f"item {n} comes before the previous one in the audio")
            last_order = order
            # look first in the item's own sentence, then in the rest of the card
            own = diac.get(target_of(at))
            texts = ([own] if own else []) + [v for k, v in diac.items() if k != target_of(at)]
            out = {"label": vowelled(cid, it["label"], texts, vocab), "at": at, "frac": round(frac, 3)}
            if it["sub"]:
                out["sub"] = "|".join(vowelled(cid, part.strip(), texts, vocab) for part in it["sub"].split("|"))
            if it.get("side") is not None:
                out["side"] = it["side"]
            if it.get("value") is not None:
                out["value"] = it["value"]
            if it["kind"]:
                out["kind"] = it["kind"]
            items.append(out)
        texts = list(diac.values())
        scene = {"type": spec["type"], "items": items}
        if spec["layout"]:
            scene["layout"] = spec["layout"]
        if spec["title"]:
            scene["title"] = vowelled(cid, spec["title"], texts, vocab)
        if spec["tone"]:
            scene["tone"] = spec["tone"]
        if spec["sides"]:
            scene["sides"] = [{"title": vowelled(cid, s["title"], texts, vocab), "tone": s["tone"]} for s in spec["sides"]]
        if spec["note"]:
            scene["note"] = vowelled(cid, spec["note"], texts, vocab)
        # per-type sanity
        if spec["type"] == "sides" and len(spec["sides"]) != 2:
            err(cid, "a sides scene needs two side titles")
        if spec["type"] == "timeline" and any(i.get("value", 0) <= 0 for i in items):
            err(cid, "timeline items need a positive value")
        if spec["type"] == "transform":
            for k in ("ring", "fill", "turn", "safe"):
                if not any(i.get("kind") == k for i in items):
                    err(cid, f"transform scene lacks a {k} item")
        if spec["type"] == "art":
            need = ART.get(spec["layout"])
            known = ART_KINDS.get(spec["layout"])
            if known is not None:
                for i in items:
                    if i.get("kind") not in known:
                        err(cid, f"illustration {spec['layout']} has no beat «{i.get('kind')}»")
            elif need is None:
                err(cid, f"unknown illustration «{spec['layout']}»")
            else:
                for k, n in need.items():
                    have = sum(1 for i in items if i.get("kind") == k)
                    if have != n:
                        err(cid, f"illustration {spec['layout']} needs {n} «{k}» beat(s), has {have}")
                if len(spec["sides"]) > 2:
                    err(cid, "an illustration takes at most two extra titles")
        if spec["type"] == "stars":
            for k in ("decor", "meteor", "guide", "rain"):
                if not any(i.get("kind") == k for i in items):
                    err(cid, f"stars scene lacks a {k} item")
        c["scene"] = scene
        done += 1
    # the quiz: one question, the right answer first then two wrong ones (the app shuffles them), and the explanation
    for c in pack["cards"]:
        entry = QUIZ.get(c["id"])
        if entry is None:
            err(c["id"], "no quiz in quiz_choices.py")
            continue
        q, right, wrong, explain = entry
        choices = [right] + list(wrong)
        if len(wrong) != 2 or len(set(choices)) != 3 or not all(x.strip() for x in choices + [q, explain]):
            err(c["id"], "a quiz needs a question, three different answers and an explanation")
        if max(len(x) for x in choices) > 60:
            notes.append(f"{c['id']}: a quiz answer is longer than 60 letters")
        c["quiz"] = {"q": q, "a": explain, "choices": choices}
    for k in QUIZ:
        if k not in cards:
            err(k, "quiz for a card that does not exist")
    used = [c["path"] for c in pack["cards"]]
    shelf = [b for b in SHELF if any(part["paths"] for part in b["parts"])]     # books with a ready part
    listed = [p for cat in CATEGORIES for p in cat["paths"]] + [p for b in shelf for part in b["parts"] for p in part["paths"]]
    for p in sorted(set(used)):
        if listed.count(p) != 1:
            err("categories", f"path «{p}» is in {listed.count(p)} categories or book parts (needs exactly one)")
    for p in set(listed) - set(used):
        err("categories", f"path «{p}» has no cards")
    for b in shelf:
        if b["book"] not in pack["books"]:
            err("shelf", f"book {b['id']}: source {b['book']} is not in the pack's books")
    pack["categories"] = CATEGORIES
    pack["shelf"] = shelf
    problems, summary = variety_report(pack["cards"], SCENES)
    print(summary)
    for v in problems:
        (err("variety", v) if STRICT else notes.append("variety: " + v))
    for n in notes:
        print("note:", n)
    if errors:
        print("\n".join("ERROR " + e for e in errors))
        sys.exit(1)
    json.dump(pack, open(path, "w", encoding="utf-8"), ensure_ascii=False)
    print(f"{len(CATEGORIES)} categories, {len(shelf)} books on the shelf and {done} scenes written to {path}")


if __name__ == "__main__":
    default = os.path.join(HERE, "..", "..", "app", "src", "main", "assets", "content", "cards.json")
    build(sys.argv[1] if len(sys.argv) > 1 else default)
