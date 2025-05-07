module MPP.project.Networking.main {
    requires com.google.gson;
    requires com.services;
    requires com.model;

    opens jsonprotocol;
    exports jsonprotocol;
}