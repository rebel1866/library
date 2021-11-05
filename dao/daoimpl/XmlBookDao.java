package by.epamtc.stanislavmelnikov.dao.daoimpl;

import by.epamtc.stanislavmelnikov.dao.daointerface.BookDao;
import by.epamtc.stanislavmelnikov.dao.exception.DaoException;
import by.epamtc.stanislavmelnikov.entity.Book;
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

public class XmlBookDao implements BookDao<Book> {
    private static final String FILE_PATH = "target\\classes\\books.xml";
    private DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

    public void createFile() throws DaoException {
        try (FileWriter fileWriter = new FileWriter(FILE_PATH, false)) {
            String text = "<books>" + "\n" + "</books>";
            fileWriter.write(text);
            fileWriter.flush();
        } catch (IOException e) {
            throw new DaoException("exception while trying to create file", e);
        }
    }

    public boolean isExists() {
        return new File(FILE_PATH).exists();
    }


    @Override
    public List<Book> getBooks(String criteria, String request) throws DaoException {
        List<Book> bookList = new ArrayList<>();
        try {
            builderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(FILE_PATH));
            document.getDocumentElement().normalize();
            NodeList list = document.getElementsByTagName("book");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                Element element = (Element) node;
                if (!criteria.equals("ALL")) {
                    String value = element.getElementsByTagName(criteria).item(0).getTextContent();
                    if (!value.toLowerCase().contains(request)) continue;
                }
                Book book = parseBookFromXml(element);
                bookList.add(book);
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new DaoException("exception while trying to get books from file", e);
        }
        return bookList;
    }

    @Override
    public void writeBookInSource(Book book) throws DaoException {
        if (!isExists()) createFile();
        try {
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(new File(FILE_PATH));
            Element rootElement = doc.getDocumentElement();
            Node bookElement = rootElement.appendChild(doc.createElement("book"));
            Element name = doc.createElement("name");
            name.setTextContent(book.getName());
            Element author = doc.createElement("author");
            author.setTextContent(book.getAuthor());
            Element year = doc.createElement("year");
            year.setTextContent(String.valueOf(book.getYear()));
            Element pages = doc.createElement("pages");
            pages.setTextContent(String.valueOf(book.getAmountPages()));
            Element id = doc.createElement("id");
            id.setTextContent(String.valueOf(book.getId()));
            createChildNodes(bookElement, name, author, year, pages, id);
            try (FileOutputStream output = new FileOutputStream(FILE_PATH)) {
                writeXml(doc, output);
            } catch (IOException | TransformerException e) {
                throw new DaoException("Writing book in file exception", e);
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new DaoException("Exception while trying to write book in file", e);
        }
    }

    @Override
    public void removeById(String requestID) throws DaoException {
        boolean isFound = false;
        try {
            builderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(FILE_PATH));
            document.getDocumentElement().normalize();
            Node books = document.getDocumentElement();
            NodeList list = document.getElementsByTagName("book");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = element.getElementsByTagName("id").item(0).getTextContent();
                    if (!id.equals(requestID)) continue;
                    books.removeChild(node);
                    isFound = true;
                }
            }
            try (FileOutputStream output = new FileOutputStream(FILE_PATH)) {
                writeXml(document, output);
            } catch (IOException | TransformerException e) {
                throw new DaoException("writing file exception", e);
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new DaoException("exception while searching book in file", e);
        }
        if (!isFound) throw new DaoException("Id is not found, operation's failed");
    }

    @Override
    public List<Integer> getAllID() throws DaoException {
        StringBuilder sourceText;
        try (FileReader fileReader = new FileReader(FILE_PATH)) {
            sourceText = new StringBuilder();
            int fig;
            while ((fig = fileReader.read()) != -1) {
                sourceText.append((char) fig);
            }
        } catch (IOException e) {
            throw new DaoException("Input/Output exception", e);
        }
        Pattern pattern = Pattern.compile("<id>(.+?)</id>");
        Matcher matcher = pattern.matcher(sourceText);
        List<Integer> ids = new ArrayList<>();
        while (matcher.find()) {
            ids.add(Integer.parseInt(matcher.group(1)));
        }
        return ids;
    }

    public static void writeXml(Document doc, OutputStream output) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);
        transformer.transform(source, result);
    }

    public Book parseBookFromXml(Element element) {
        String name = element.getElementsByTagName("name").item(0).getTextContent();
        String author = element.getElementsByTagName("author").item(0).getTextContent();
        String year = element.getElementsByTagName("year").item(0).getTextContent();
        String pages = element.getElementsByTagName("pages").item(0).getTextContent();
        String id = element.getElementsByTagName("id").item(0).getTextContent();
        Book book = new Book(name, author, Integer.parseInt(year), Integer.parseInt(pages), Integer.parseInt(id));
        return book;
    }

    public void createChildNodes(Node bookElement, Element name, Element author, Element year, Element pages,
                                 Element id) {
        bookElement.appendChild(name);
        bookElement.appendChild(author);
        bookElement.appendChild(year);
        bookElement.appendChild(pages);
        bookElement.appendChild(id);
    }
}
