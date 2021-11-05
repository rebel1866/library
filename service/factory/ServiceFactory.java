package by.epamtc.stanislavmelnikov.service.factory;

import by.epamtc.stanislavmelnikov.service.serviceinterface.ClientService;
import by.epamtc.stanislavmelnikov.service.serviceimpl.ClientServiceImpl;
import by.epamtc.stanislavmelnikov.service.serviceinterface.LibraryService;
import by.epamtc.stanislavmelnikov.service.serviceimpl.LibraryServiceImpl;

public class ServiceFactory {
    private static final ServiceFactory instance = new ServiceFactory();

    private ClientService clientService = new ClientServiceImpl();
    private LibraryService libraryService = new LibraryServiceImpl();

    private ServiceFactory() {

    }

    public static ServiceFactory getInstance() {
        return instance;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public void setLibraryService(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public LibraryService getLibraryService() {
        return libraryService;
    }
}
