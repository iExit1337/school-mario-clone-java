package ostboysnrun;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ostboysnrun.levels.AbstractLevel;
import ostboysnrun.levels.LevelRegistry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ProgressManager {

    private static ArrayList<AbstractLevel> unlockedLevels = new ArrayList<>();

    private static String FILE = Helper.getPath("save.xml").getPath();

    public static void addLevel(final AbstractLevel level) {
        if (!unlockedLevels.contains(level)) {
            unlockedLevels.add(level);
        }
    }

    public static ArrayList<AbstractLevel> getUnlockedLevels() {
        return unlockedLevels;
    }

    public static void load() throws IOException, SAXException, ParserConfigurationException {
        final File file = new File(FILE);
        final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        final Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();

        // levels
        final NodeList nodeList = doc.getElementsByTagName("level");
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                final Element element = (Element) node;
                final int levelId = Integer.parseInt(element.getAttribute("id"));
                addLevel(LevelRegistry.byId(levelId));
            }
        }
    }

    public static boolean save() {
        try {
            final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            final Document doc = docBuilder.newDocument();
            final Element rootElement = doc.createElement("levels");
            doc.appendChild(rootElement);

            for (final AbstractLevel l : unlockedLevels) {
                final Element level = doc.createElement("level");
                level.setAttribute("id", "" + l.getLevelId());
                rootElement.appendChild(level);
            }

            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            final DOMSource source = new DOMSource(doc);
            final StreamResult result = new StreamResult(FILE);

            transformer.transform(source, result);

            return true;
        } catch (ParserConfigurationException | TransformerException e) {
            //e.printStackTrace();
            return false;
        }
    }

    public static boolean hasLevelUnlocked(final int id) {
        return unlockedLevels.contains(LevelRegistry.byId(id));
    }
}
