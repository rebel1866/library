package by.epamtc.stanislavmelnikov.dao.daoimpl;

import by.epamtc.stanislavmelnikov.dao.daointerface.UserDao;
import by.epamtc.stanislavmelnikov.dao.exception.DaoException;
import by.epamtc.stanislavmelnikov.entity.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlUserDao implements UserDao<User> {
    private static final String FILEPATH = "target\\classes\\users.xml";
    private DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

    @Override
    public User getUser(String loginReq) throws DaoException {
        try {
            builderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(FILEPATH));
            document.getDocumentElement().normalize();
            User user = null;
            NodeList list = document.getElementsByTagName("user");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String login = element.getElementsByTagName("login").item(0).getTextContent();
                    if (!login.equals(loginReq)) continue;
                    String password = getElementByTag(element, "password");
                    String firstName = getElementByTag(element, "firstname");
                    String lastName = getElementByTag(element, "lastname");
                    String age = getElementByTag(element, "age");
                    String email = getElementByTag(element, "email");
                    String isAdmin = getElementByTag(element, "admin");
                    user = new User();
                    setUserFields(user, login, password, firstName, lastName, age, email, isAdmin);
                }
            }
            if (user == null) throw new DaoException("User is not found");
            return user;
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new DaoException("exception while parsing file", e);
        }
    }

    @Override
    public void writeUserInFile(User user) throws DaoException {
        try {
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(new File(FILEPATH));
            doc.getDocumentElement().normalize();
            Element rootElement = doc.getDocumentElement();
            Node userElement = rootElement.appendChild(doc.createElement("user"));
            Element login = doc.createElement("login");
            login.setTextContent(user.getLogin());
            Element password = doc.createElement("password");
            password.setTextContent(user.getPassword());
            Element firstName = doc.createElement("firstname");
            firstName.setTextContent(user.getFirstName());
            Element lastName = doc.createElement("lastname");
            lastName.setTextContent(user.getLastName());
            Element age = doc.createElement("age");
            age.setTextContent(String.valueOf(user.getAge()));
            Element email = doc.createElement("email");
            email.setTextContent(user.getEmail());
            Element admin = doc.createElement("admin");
            admin.setTextContent(String.valueOf(user.isAdmin()));
            createChildNodes(userElement, login, password, firstName, lastName, age, email, admin);
            try (FileOutputStream output = new FileOutputStream(FILEPATH)) {
                writeXml(doc, output);
            } catch (IOException | TransformerException e) {
                throw new DaoException("exception while writing file", e);
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new DaoException("Adding user exception", e);
        }
    }

    @Override
    public void removeByLogin(String loginReq) throws DaoException {
        boolean isFound = false;
        try {
            builderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(FILEPATH));
            NodeList list = document.getElementsByTagName("user");
            Node users = document.getDocumentElement();
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String login = element.getElementsByTagName("login").item(0).getTextContent();
                    if (!login.equals(loginReq)) continue;
                    users.removeChild(node);
                    isFound = true;
                }
            }
            try (FileOutputStream output = new FileOutputStream(FILEPATH)) {
                writeXml(document, output);
            } catch (IOException | TransformerException e) {
                throw new DaoException("exception while trying to write xml into file", e);
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new DaoException("exception while trying to remove user from file", e);
        }
        if (!isFound) throw new DaoException("Login is not found");
    }

    @Override
    public List<String> getLogins() throws DaoException {
        StringBuilder sourceText;
        try (FileReader fileReader = new FileReader(FILEPATH)) {
            sourceText = new StringBuilder();
            int fig;
            while ((fig = fileReader.read()) != -1) {
                sourceText.append((char) fig);
            }
        } catch (IOException e) {
            throw new DaoException("Input/Output exception", e);
        }
        Pattern pattern = Pattern.compile("<login>(.+?)</login>");
        Matcher matcher = pattern.matcher(sourceText);
        List<String> logins = new ArrayList<>();
        while (matcher.find()) {
            logins.add(matcher.group(1));
        }
        return logins;
    }

    public static void writeXml(Document doc, OutputStream output) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);
        transformer.transform(source, result);
    }

    public void setUserFields(User user, String login, String password, String firstName, String lastName,
                              String age, String email, String isAdmin) {
        user.setLogin(login);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAge(Integer.parseInt(age));
        user.setEmail(email);
        user.setAdmin(Boolean.parseBoolean(isAdmin));
    }

    public String getElementByTag(Element element, String tag) {
        return element.getElementsByTagName(tag).item(0).getTextContent();
    }

    public void createChildNodes(Node userElement, Element login, Element password, Element firstName, Element lastName,
                                 Element age, Element email, Element admin) {
        userElement.appendChild(login);
        userElement.appendChild(password);
        userElement.appendChild(firstName);
        userElement.appendChild(lastName);
        userElement.appendChild(age);
        userElement.appendChild(email);
        userElement.appendChild(admin);
    }
}
