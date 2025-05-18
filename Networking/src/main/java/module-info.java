module MPP.project.Networking.main {
    requires com.google.gson;
    requires com.services;
    requires com.model;

    opens jsonprotocol;
    opens dto;
    exports jsonprotocol;
    exports dto;
}