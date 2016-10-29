package com.github.brunodles.githubpopular.app.application;


import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.inspector.database.DefaultDatabaseConnectionProvider;
import com.facebook.stetho.inspector.database.DefaultDatabaseFilesProvider;
import com.facebook.stetho.inspector.database.SqliteDatabaseDriver;
import com.facebook.stetho.inspector.protocol.module.Database;

/**
 * Created by bruno on 29/10/16.
 */
final class ApplicationMods {
    static void apply(Application application) {
        Stetho.Initializer initializer = Stetho.newInitializerBuilder(application)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(application))
                .enableWebKitInspector(() -> new Stetho.DefaultInspectorModulesBuilder(application)
                        .provideDatabaseDriver(genericDatabaseDriver(application))
                        .finish())
                .build();
        Stetho.initialize(initializer);
    }

    private static Database.DatabaseDriver genericDatabaseDriver(Application application) {
        return new SqliteDatabaseDriver(application,
                new DefaultDatabaseFilesProvider(application),
                new DefaultDatabaseConnectionProvider()
        );
    }
}
