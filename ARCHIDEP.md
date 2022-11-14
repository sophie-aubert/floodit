# Deploy Flood It, a Spring Boot (Java) & Angular (JavaScript) application with a PostgreSQL database

The goal of this exercice is to put in practice the knowledge acquired during
previous exercices to deploy a new application from scratch on your server.

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## The goal

You must deploy the provided application in a similar way as the PHP todolist in
previous exercises:

- Install the language(s) and database necessary to run the application (which
  are different than for the PHP todolist).
- Run the application as a systemd service.
- Serve the application through nginx acting as a reverse proxy.
- Provision a TLS certificate for the application and configure nginx to use it.
- Set up an automated deployment via Git hooks for this application.

Additionally:

- The application **MUST** run in production mode (see its documentation).
- The application **MUST** restart automatically if your server is rebooted
  (i.e. your systemd service must be enabled).
- The application **MUST** be accessible **only through nginx**. It **MUST NOT**
  be exposed directly on a publicly accessible port. In the cloud servers used
  in this course, ports 3000 and 3001 should be open for testing. **DO NOT** use
  these ports in the final setup.
- Clients accessing the application over HTTP **MUST** be redirected to HTTPS.

As an optional bonus challenge:

- Create a dedicated Unix user (e.g. `floodit`) other than your personal user
  (e.g. `john_doe`) to run the application.
- This user must be a system user, not a login user. It must not be able to log
  in with a password, although you can set up SSH public key authentication for
  the automated deployment.
- Clone the project's repository with the dedicated user instead of your
  personal user.
- Configure systemd to run the application as the dedicated user instead of your
  personal user.
- Set up the automated deployment with the dedicated user instead of your
  personal user.
- Use the application's local configuration file instead of environment
  variables (see its documentation), and set up file/directory permissions so
  that only the dedicated user has access to the configuration file (the `root`
  user will of course have access as well).

## Legend

Parts of this guide are annotated with the following icons:

- :exclamation: A task you **MUST** perform to complete the exercise.
- :question: An optional step that you _may_ perform to make sure that
  everything is working correctly. But you can skip it because it is not
  necessary.
- :warning: **Critically important information about the exercise.**
- :gem: Tips on the exercise, reminders about previous exercises, or
  explanations about how this exercise differs from the previous one.
- :space_invader: More advanced tips on how to save some time, or tips about the
  bonus challenge.
- :books: Additional information that you can read if you want to know more
  about the commands and tools used during this exercise.
- :checkered_flag: The end of the exercise.
  - :classical_building: The architecture of what you deployed during the
    exercise.
- :boom: Troubleshooting tips: how to fix common problems you might encounter.

## The application

The application you must deploy is a small web game. Its code is [available on
GitHub][repo].

### :books: What the hell are Spring Boot & Angular?

The application uses the following ~~buzzword salad~~ technologies:

- The backend has been developed with [Spring Boot][spring-boot], a Java
  framework that makes it easy to create stand-alone, production-grade Spring
  based Applications that you can "just run".
  - [Spring][spring] makes programming Java quicker, easier, and safer for
    everybody. Spring's focus on speed, simplicity, and productivity has made it
    the world's most popular Java framework.
  - [Java][java] is a popular programming language and development platform. It
    reduces costs, shortens development timeframes, drives innovation, and
    improves application services. With millions of developers running more than
    60 billion Java Virtual Machines worldwide, Java continues to be the
    development platform of choice for enterprises and developers.
- The frontend has been developed with [Angular][angular], a [JavaScript][js]
  application-design framework and development platform for creating efficient
  and sophisticated single-page apps.
- [PostgreSQL][postgres] is a powerful, open source object-relational database
  system with over 30 years of active development that has earned it a strong
  reputation for reliability, feature robustness, and performance.

You do not need to know the specifics of these technologies. Your goal is only
to deploy the application as indicated by the instructions. You will not need to
modify it except for a very small change at the end to test your automated
deployment.

## :warning: Before starting the exercise

**Your Azure server has limited memory (about 1GB of RAM).** Unfortunately, this
may not be enough memory to run the MySQL database server, the PostgreSQL
database server, PHP-FPM, the PHP todolist and the Flood It application,
mainly because MySQL unfortunately consumes a lot of memory for such a small
server.

To be safe, you should temporarily stop and disable MySQL with the following
commands:

```bash
$> sudo systemctl stop mysql
$> sudo systemctl disable mysql
```

Note that this will temporarily break the PHP todolist.

You can also stop, disable and remove the following programs which are not
required for this course, saving some more memory:

```bash
$> sudo systemctl stop gdm
$> sudo systemctl disable gdm
$> sudo apt remove snapd --purge
```

## :warning: A note about the project's documentation

The [project's README][readme] explains how to set up and run the application.
That README is generic: it is not written specifically for this course or this
exercise.

The instructions on **this page** explain the exercise step-by-step.

The instructions in the project's README will be useful to you at various
points, but be careful not to blindly copy-paste and execute commands from it
without understanding what you are doing in the context of the exercise.

## :exclamation: Getting started

### :exclamation: Fork the repository

You must [fork][fork] the [application's repository][repo] to your own GitHub
account, because this exercise requires that you make changes to the application
later, after setting up the automated deployment with Git hooks.

:warning: Use your **own** repository's SSH clone URL. This way you will have
push access to your repository.

### :exclamation: Install the requirements

You may want to start by making sure you have installed all the requirements
described in the [project's README][readme] on your server:

- **How to install Java:** there are several methods to install Java. Java was
  originally developed by [Sun Microsystems][sun] and now by [Oracle][oracle],
  but there are also free, open source implementations. We suggest you use
  [OpenJDK][openjdk], one of the most popular open source implementations
  originally released by Sun.

  The OpenJDK publishes easy-to-install APT packages. You can list then with:

  ```bash
  $> apt search openjdk-
  ```

  You should install a package named `openjdk-<version>-jdk` where `<version>`
  is the Java version required by the Flood It application.

  > :books: TODO: jre vs jdk

- **How to install Node.js:** there are several methods to install Node.js. One
  of the simplest is to use the [binary distributions provided by
  NodeSource][node-install]. You should look for installation instructions
  specific to Ubuntu, the Linux distribution used on your server. If possible,
  you should find instructions for the apt package manager (using the `apt` or
  `apt-get` command).
- **How to install PostgreSQL:** you can follow the official instructions on the
  Downloads page of the [PostgreSQL website][postgres]. You should look for
  installation instructions specific to Ubuntu, the Linux distribution used on
  your server.

#### :question: Check that everything has been correctly installed

- You can check that **Java has been correctly installed** by displaying the
  version of the `java` command:

  ```bash
  $> java -version
  openjdk version "18.0.2" 2022-07-19
  OpenJDK Runtime Environment (build 18.0.2+9-61)
  OpenJDK 64-Bit Server VM (build 18.0.2+9-61, mixed mode, sharing)
  ```

- You can check that **Node.js has been correctly installed** by displaying the
  version of the `node` command:

  ```bash
  $> node --version
  v18.7.0
  ```

  > It's not a problem if you don't have this exact version installed, as long
  > as you have version 18.x.

  You can also check that Node.js is working correctly by running the following
  command:

  ```bash
  $> node -e 'console.log(1 + 2)'
  3
  ```

- You can check that **PostgreSQL has been correctly installed** by displaying
  the version of the `psql` command:

  ```bash
  $> psql --version
  psql (PostgreSQL) 12.9 (Ubuntu 12.9-0ubuntu0.20.04.1)
  ```

  You can verify that PostgreSQL is running by showing the status of its Systemd
  service:

  ```bash
  $> sudo systemctl status postgresql
  ● postgresql.service - PostgreSQL RDBMS
      Loaded: loaded (/lib/systemd/system/postgresql.service; enabled; vendor preset: enabled)
      Active: active (exited) since Fri 2021-12-10 20:54:52 UTC; 3 days ago
    Main PID: 2724 (code=exited, status=0/SUCCESS)
        Tasks: 0 (limit: 1087)
      Memory: 0B
      CGroup: /system.slice/postgresql.service

  Dec 10 20:54:52 john-doe.archidep.ch systemd[1]: Starting PostgreSQL RDBMS...
  Dec 10 20:54:52 john-doe.archidep.ch systemd[1]: Finished PostgreSQL RDBMS.
  ```

  You can also verify that PostgreSQL is working by listing available databases,
  also with the `psql` command:

  ```bash
  $> sudo -u postgres psql -l
                                List of databases
     Name    |  Owner   | Encoding | Collate |  Ctype  |   Access privileges
  -----------+----------+----------+---------+---------+-----------------------
   postgres  | postgres | UTF8     | C.UTF-8 | C.UTF-8 |
   template0 | postgres | UTF8     | C.UTF-8 | C.UTF-8 | =c/postgres          +
             |          |          |         |         | postgres=CTc/postgres
   template1 | postgres | UTF8     | C.UTF-8 | C.UTF-8 | =c/postgres          +
             |          |          |         |         | postgres=CTc/postgres
  (3 rows)
  ```

  > Note that **PostgreSQL runs on port 5432 by default**, which you can verify
  > by checking the `port` setting in its configuration file:
  >
  > ```bash
  > $> cat /etc/postgresql/15/main/postgresql.conf | grep '^port'
  > port = 5432
  > ```

### :exclamation: Perform the initial setup

You must perform the **initial setup** instructions indicated in the [project's
README][readme].

> - :gem: The Flood It application has two configuration mechanisms: environment
>   variables or a local configuration file. You can use either one of them. It
>   does not matter which you choose. Both are equally valid way of configuring
>   the application.
>
>   If you choose to use environment variables as you have done with the PHP
>   todolist, you will need to provide these environment variables through
>   Systemd later.
>
>   The `export` sample commands provided in the README will only set the
>   variables in the shell/SSH session where you run them. You will need to run
>   them again in each new shell, or add them to your shell's startup
>   configuration file (`.bash_profile`) on the server.

### :books: What sorcery is this?

:books: The setup instructions use the `createuser` and `createdb` commands.
These commands are binaries that come with the PostgreSQL server and can be used
to manage PostgreSQL users and databases on the command line:

- The **`createuser --interactive --pwprompt floodit` command** creates a
  PostgreSQL user named "floodit" and asks you to define a password for that
  user. The application will use this PostgreSQL username and password to
  connect to the database.
- The **`createdb --owner floodit floodit` command** creates an empty PostgreSQL
  database named "floodit" and owned by the "floodit" user. This is the database
  that the application will use.

:books: This setup is equivalent to [part of the `todolist.sql`
script](https://github.com/MediaComem/comem-archidep-php-todo-exercise/blob/5d46e9fcf974d3d74d5eec838c512798f02581e1/todolist.sql#L1-L8)
you executed when first deploying the PHP todolist.

If you prefer using SQL, you could instead connect to the database as the
`postgres` user (equivalent to MySQL's `root` user) with `sudo -u postgres psql`
and run equivalent [`CREATE USER`](https://www.postgresql.org/docs/13/sql-createuser.html) and [`CREATE DATABASE`](https://www.postgresql.org/docs/13/sql-createdatabase.html) queries.

Note that on the command line, PostgreSQL uses [peer
authentication](https://www.postgresql.org/docs/13/auth-peer.html) based on the
Unix username by default. This is why the commands are prefixed with `sudo -u postgres` to execute them as the `postgres` Unix user. This user was created
when you installed PostgreSQL and has administrative privileges on the entire
PostgreSQL cluster. You can verify the existence of this user with the command
`cat /etc/passwd | grep postgres`.

:books: The setup instructions use the **`mvn` command**. [Maven][mvn] is a
software project management tool for the [Java][java] ecosystem, much like
[Composer][composer] for [PHP][php] or [npm][npm] for [Node.js][node].

- A Maven project has one or several Project Object Model (POM) files. These
  `pom.xml` files describe a project's dependencies and how to build it (you can
  look at the Flood It application's `backend/pom.xml` file as an example).
- The **`mvn clean install -Pskip-test` command** is used to:

  - Download all of the Flood It application's dependencies (i.e. the Java
    libraries it requires to work), like the [Spring Boot][spring-boot] web
    framework. Spring Boot is a web framework written in [Java][java] much
    like [Laravel][laravel] is a web framework written in [PHP][php].

    The dependencies are downloaded from [Maven Central][mvn-central],
    the main package registry for the Elixir and Erlang ecosystems, and saved into the
    `deps` directory.

  - Build the application (i.e. compile the Java source code into Java Virtual
    Machine bytecode).
  - Install the application into the local Maven repository.

- TODO: explain other Java commands

:books: The configuration you are instructed to perform either through
environment variables or through the
`backend/config/application-default.local.yml` file is equivalent to the
[configuration of the PHP
todolist](https://github.com/MediaComem/comem-archidep-php-todo-exercise/blob/5d46e9fcf974d3d74d5eec838c512798f02581e1/index.php#L3-L15)
which you improved during the course using environment variables. It is not
uncommon for applications to provide multiple configuration mechanisms, letting
you choose which is more convenient for you.

### :question: Optional: run the automated tests

The Flood It application includes an automated test suite. [Automated
tests][automated-tests] are programs that check that the application works by
simulating input and checking output. They are not a replacement for manual
testing by humans, but programs can test mundane, repetitive tasks much faster
and much more reliably than a human can.

The [project's README][readme] explains how to set up and run the automated
tests.

Running these tests is entirely optional, but it will make sure that everything
is working properly, including that:

- The application executes correctly with the Java Runtime Environment (JRE) you
  have installed.
- The application can successfully connect to and migrate the database.
- The application behaves as specified.

> :books: If you are curious, the source code for these tests is in [the `test`
> directory](https://github.com/MediaComem/floodit/tree/main/backend/src/main/test).

### :question: Optional: run the application in development mode

Before running the application in production mode and attempting to set up the
systemd service, nginx configuration and automated deployment, you can manually
run the application in development mode to make sure it works. The [project's
README][readme] explains how to do this.

You can set the `FLOODIT_SERVER_PORT` environment variable or the `server.port`
parameter in the local configuration file to `3001` for this simple test, as
that is one of the ports that should be open in your server's firewall. Run the
application on that port and visit http://W.X.Y.Z:3001 to check that it works
(replacing `W.X.Y.Z` by your server's IP address). Stop the application by
typing `Ctrl-C` once you are done.

TODO: explain frontend

### :exclamation: Run the application in production mode

Follow the instructions in the [project's README][readme] to run the application
in production mode.

> :books: You will once again recompile the application, this time in production
> mode.
>
> :books: To run a Maven project in production, you must install it (i.e. the
> `mvn clean install` command), which will create a JAR file.. This is basically
> a ZIP file of the compiled Java application, which can be run by any Java
> Virtual Machine (JVM). Once you have created that JAR file, you could copy it
> to any system that has the Java Runtime Environment (JRE) and run it there.
> The Java Development Kit (JDK) is only required to perform the compilation.
>
> :books: The `npm run build` command used in the instructions bundles the
> frontend's files in production mode, compressing and digesting them. To
> "digest" a web asset is to include a hash of its contents in the filename [for
> the purposes of caching][webpack-caching]. This optimizes the delivery of web
> assets to browsers especially when they come back to your website after having
> already visited once.
>
> :books: You can list the `frontend/dist` directory to see the digested assets:
> `ls frontend/dist`. Observe that a file named `runtime.a185471aecb581c1.js`
> (the hash may differ) has appeared. The hash part of the filename
> (`a185471aecb581c1` in this case) depends on the content. When the content
> changes, the hash changes. This means you can instruct client browsers to
> cache web assets indefinitely, since you know that an asset's name will not
> change as long as its content does not change as well and, conversely, that an
> asset's name will always change if it has been modified.

## :exclamation: Create a systemd service

Create and enable a systemd unit file like in the [systemd
exercise][systemd-ex]. Make the necessary changes to run the Flood It
application instead of the PHP todolist.

> - :gem: You will find the correct command to run the application in [the
>   project's `README`][readme].
>
>   Remember that systemd requires absolute paths to commands. You can use
>   `which <command>` to determine where a command is.
>
> - :gem: You may need to set the `FLOODIT_SERVER_PORT` environment variable or
>   the `server.port` parameter in the local configuration file to choose the
>   port on which the application will listen. You can use the publicly
>   accessible 3001 port temporarily for testing, but you should use another
>   free port that is not exposed to complete the exercise, since one of the
>   requirements is to expose the application only through nginx.

Once you have enabled and started the service, it should start automatically the
next time you restart the server with `sudo reboot`.

> :space_invader: If you know what you are doing, you can already set up the
> automated deployment project structure at this point, so that you can point
> your systemd configuration to the correct directory. That way you will not
> have to modify it later.

## :exclamation: Serve the application through nginx

Create an nginx proxy configuration to serve the application like in the [nginx
PHP-FPM exercise][nginx-php-fpm-ex].

The `root` directive in your nginx configuration should point to the
`frontend/dist` directory in the repository since that is the directory that
contains the application's public web assets.

> - :gem: Use an absolute path for the `root` directive.
> - :gem: Do not follow steps related to PHP FPM, since they are only valid for
>   a PHP application.
> - :gem: The `include` and `fastcgi_pass` directives used in the PHP FPM
>   exercise make no sense for a non-PHP application. You should replace them
>   with a [`proxy_pass`
>   directive](http://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_pass).
>   as [presented during the course][nginx-rp-conf].
> - :space_invader: You can also point the nginx configuration directly to the
>   automated deployment structure. That way you will not have to modify it
>   later.

## :exclamation: Provision a TLS certificate

Obtain and configure a TLS certificate to serve the application over HTTPS like
in the [certbot exercise][certbot-ex].

## :exclamation: Set up an automated deployment with Git hooks

Change your deployment so that the application can be automatically updated via
a Git hook like in the [automated deployment exercise][auto-deploy-ex].

Once you have set up the new directories, make sure to update your systemd unit
file and nginx configuration file to point to the correct directories.

Because the new directory is a fresh deployment, you may have to repeat part of
the [initial setup][initial-setup] you performed in the original directory. The
PostgreSQL user, database and extension have already been created, and your hook
will handle most of the rest of the setup. But if you used the
`backend/config/application-default.local.yml` configuration file, you must copy
it to the new deployment directory as well. You can use the `cp <source> <target>` command for this.

Complete the `post-receive` hook. Compared to the PHP todolist, there are
additional steps which must be performed in the script for the automated
deployment to work correctly:

- Frontend dependencies must be updated in case there are new or upgraded ones.
  The PHP todolist had no dependencies so you did not need to do this.

  > :books: The backend dependencies of the Flood It application must also be
  > updated, but Maven will do this for you automatically.

- The Angular frontend must be rebuilt in case changes were made to the
  frontend source files.
- The backend application must be rebuilt in case changes were made to the
  source files.
- The systemd service must be restarted with `systemctl`.

  > :books: Unlike PHP, code in most other languages is not reinterpreted
  > on-the-fly; the service must be restarted so that the program is reloaded
  > into memory as a new process).

The [project's README][readme] explains how to do all of this except restarting
the Systemd service, which you can easily do with `sudo systemctl restart <service>`. You should run the appropriate commands in your `post-receive` hook
script.

### :gem: Allowing your user to restart the service without a password

In order for the new `post-receive` hook to work, your user must be able to run
`sudo systemctl restart floodit` (assuming you have named your service
`floodit`) without entering a password, otherwise it will not work in a Git
hook.

> :books: This is because a Git hook is not an interactive program. You are not
> running it yourself, so you are not available to enter your password where
> prompted.

If you are using the administrator user account that came with your Azure VM to
run the application, it already has the right to use `sudo` without a password.

> :books: This has been automatically configured for you in the
> `/etc/sudoers.d/90-cloud-init-users` file.

### :space_invader: Allowing the dedicated `floodit` Unix user to control the Systemd service

If you are trying to complete the bonus challenge, you will need to allow the
`floodit` user run the necessary `sudo systemctl ...` commands without a
password as well.

Make sure your default editor is `nano` (or whichever you are more comfortable
with):

```bash
$> sudo update-alternatives --config editor
```

When you created the `floodit` Unix user, your server created a
corresponding Unix group with the same name by default. Now you will add a file
in the `/etc/sudoers.d` directory to allow users in the `floodit` Unix group
to run some specific commands without a password.

```bash
$> sudo visudo -f /etc/sudoers.d/floodit
```

> :books: The [`visudo` command][visudo] allows you to edit the sudoers file in
> a safe fashion. It will refuse to save a sudoers file with a syntax error
> (which could potentially corrupt your system or lock you out of your
> administrative privileges).

Add the following line to the file:

```
%floodit ALL=(ALL:ALL) NOPASSWD: /bin/systemctl restart floodit, /bin/systemctl status floodit, /bin/systemctl start floodit, /bin/systemctl stop floodit
```

> :books: This line allows any user in the `floodit` group to execute the
> listed commands with `sudo` without having to enter a password (hence the
> `NOPASSWD` option).

Exit with `Ctrl-X` if you are using Nano or with Esc then `:wq` if you are using
Vim.

> :gem: If you are using nano, the filename you are asked to confirm will be
> `/etc/sudoers.d/floodit.tmp` instead of `/etc/sudoers.d/floodit`. This is
> normal, because `visudo` uses a temporary file to validate your changes before
> saving the actual file. You may confirm without changes.

You can test that it works by first switching to the `floodit` user with `sudo su - floodit` and then running `sudo systemctl status floodit`. It should run
the command without asking you for any password (only for the specific commands
listed in the file your created).

### :exclamation: Test the automated deployment

Clone your fork of the repository to your local machine, make sure you have
added a remote pointing to your server, then commit and push a change to test
the automated deployment.

Here's some visible changes you could easily make:

- Change the [navbar title in the
  `frontend/src/app/layout/navbar/navbar.component.html`
  file](https://github.com/AlphaHydrae/floodit/blob/04cf2bdad154aae574d974d7984d8d19e4dcb504/frontend/src/app/layout/navbar/navbar.component.html#L3).
- Change the [difficulty levels in the
  `frontend/src/app/pages/dashboard/dashboard.component.html`
  file](https://github.com/AlphaHydrae/floodit/blob/04cf2bdad154aae574d974d7984d8d19e4dcb504/frontend/src/app/pages/dashboard/dashboard.component.html#L7-L38).

## :exclamation: Notify the teacher

Send an email or a Teams message to both teachers **no later than December 13th
2022 at 23:59 CET** indicating that you have finished the exercise.

## :checkered_flag: What have I done?

You have deployed a new backend/frontend web application to your server from
scratch, using the knowledge you acquired during previous deployment exercises.

### :classical_building: Architecture

This is a simplified architecture of the main running processes and
communication flow at the end of this exercise (after completing [all previous
course exercises][archidep-exercises]):

![Simplified architecture](floodit-deployment-simplified.png)

> [Simplified architecture PDF version](floodit-deployment-simplified.pdf).

## :boom: Troubleshooting

Here's a few tips about some problems you may encounter during this exercise.
Note that some of these errors can happen in various situations:

- When running a command manually from your terminal.
- When systemd tries to start your service.
- When your `post-receive` Git hook executes.

### :boom: Maven command in wrong directory

TODO: attempt to run maven command in wrong directory

### :boom: Spring Boot wrong directory

TODO: attempt to run application in wrong directory

### :boom: `password authentication failed for user "floodit"`

TODO: try using the wrong database connection configuration

If you see an error similar to this when migrating the database or starting the
application:

```
[error] Postgrex.Protocol (#PID<0.351.0>) failed to connect: ** (Postgrex.Error) FATAL 28P01 (invalid_password) password authentication failed for user "minesweeper"
```

It means that the Flood It application or its database migration scripts cannot
connect to the database:

- Are you sure that you followed all the setup instructions and performed all
  necessary configuration?
- Did you properly create the `floodit` PostgreSQL user and database?
- Did you properly configure the database connection with the
  `$FLOODIT_DATABASE_*` environment variable or via the
  `backend/config/application-default.local.yml` file?

  Are you using the correct password?

> Just like the PHP todolist required the correct configuration to successfully
> connect to its MySQL database, the Flood It application also requires the
> correct configuration to connect to its PostgreSQL database.

### :boom: `:eaddrinuse (address already in use)`

TODO: try to run the application on the same port twice

If you see an error similar to this when running the application:

```
[error] Failed to start Ranch listener MinesweeperWeb.Endpoint.HTTP in :ranch_tcp:listen([cacerts: :..., key: :..., cert: :..., port: 3000]) for reason :eaddrinuse (address already in use)

[notice] Application minesweeper exited: Minesweeper.Application.start(:normal, []) returned an error: shutdown: failed to start child: MinesweeperWeb.Endpoint
    ** (EXIT) shutdown: failed to start child: {:ranch_listener_sup, MinesweeperWeb.Endpoint.HTTP}
        ** (EXIT) shutdown: failed to start child: :ranch_acceptors_sup
            ** (EXIT) {:listen_error, MinesweeperWeb.Endpoint.HTTP, :eaddrinuse}
[notice] Application phoenix_live_reload exited: :stopped
[notice] Application file_system exited: :stopped
[notice] Application plug_cowboy exited: :stopped
[notice] Application cowboy_telemetry exited: :stopped
[notice] Application cowboy exited: :stopped
[notice] Application ranch exited: :stopped
[notice] Application cowlib exited: :stopped
[notice] Application jason exited: :stopped
[notice] Application phoenix_html exited: :stopped
[notice] Application postgrex exited: :stopped
[notice] Application ecto_sql exited: :stopped
[notice] Application db_connection exited: :stopped
[notice] Application connection exited: :stopped
[notice] Application phoenix_ecto exited: :stopped
[notice] Application ecto exited: :stopped
[notice] Application decimal exited: :stopped
[notice] Application runtime_tools exited: :stopped
** (Mix) Could not start application minesweeper: Minesweeper.Application.start(:normal, []) returned an error: shutdown: failed to start child: MinesweeperWeb.Endpoint
    ** (EXIT) shutdown: failed to start child: {:ranch_listener_sup, MinesweeperWeb.Endpoint.HTTP}
        ** (EXIT) shutdown: failed to start child: :ranch_acceptors_sup
            ** (EXIT) {:listen_error, MinesweeperWeb.Endpoint.HTTP, :eaddrinuse}
```

It means that there is already an application or other process listening on the
port Flood It is trying to listen on (port `3000` by default). You should use
the `$FLOODIT_SERVER_PORT` environment variable or the `server.port` parameter
in the local configuration file to change the port, for example if you are
trying to run the application in development mode:

```bash
$> FLOODIT_SERVER_PORT=4321 mix phx.server
```

### :boom: `remote: sudo: no tty present and no askpass program specified`

If you see an error message similar to this when your Git hook is triggered:

```
remote: sudo: no tty present and no askpass program specified
```

It means that you have created a dedicated Unix user but you have not performed
the following step correctly: [Allowing the dedicated `floodit` Unix user to
control the Systemd
service](#space_invader-allowing-the-dedicated-floodit-unix-user-to-control-the-systemd-service).

Make sure that the list of authorized `systemctl` commands in the sudoers file
match the name of your service (if you named your systemd configuration file
something other than `floodit.service`, you must adapt the commands in the
`/etc/sudoers.d/floodit` file to use the correct service name).

> :books: This error occurs because ordinarily, a Unix user does not have the
> right to execute `sudo systemctl restart floodit` without entering their
> password to gain administrative rights. A Git hook is executed in a
> non-interactive context: it can only print information, and you cannot
> interact with it (e.g. give it input) while it is running. This means that it
> cannot ask for your password, so any `sudo` command will fail by default.
>
> This is what the error message indicates: `no tty present` means that there is
> no interactive terminal (`tty` comes from the terminology of the 1970s: it
> means a **t**ele**ty**pewriter, which was one of the first terminals).
>
> The linked instructions above grant the user the right to execute specific
> `sudo` commands (like `sudo systemctl restart floodit`) without having to
> enter your password. Once that is done, these commands will work from the Git
> hook as well.

### :boom: `code=exited, status=200/CHDIR`

If you see an error message similar to this in your systemd service's status:

```
code=exited, status=200/CHDIR
```

It means that systemd failed to move into the directory you specified (`CHDIR`
means **ch**ange **dir**ectory). Check your Systemd unit file to make sure that
the working directory you have configured is the correct one and really exists.

### :boom: `502 Bad Gateway`

If you get a [502 Bad Gateway][http-502] error in your browser when trying to
access an nginx site you have configured, it means that you have reached nginx,
but that nginx could not reach the proxy address you have configured. The proxy
address is defined with the [`proxy_pass` directive][nginx-proxy-pass] in that
site's configuration file.

Are you sure that your nginx configuration, namely the proxy address, is
correct? Check to make sure you are using the correct address and port. Is your
application actually listening on that port?

### :boom: I forgot to fork the Flood It repository and I have already cloned it

You may have cloned the exercise's repository directly:

```bash
$> git remote -v
origin  https://github.com/MediaComem/floodit.git (fetch)
origin  https://github.com/MediaComem/floodit.git (push)
```

Then you won't have push access because this repository does not belong to you.
[Fork][fork] the repository, then change your clone's remote URL by running this
command in your clone's directory on the server (replacing `MyGitHubUser` with
your GitHub username):

```bash
$> git remote set-url origin https://github.com/MyGitHubUser/floodit.git
```

### :boom: I don't remember the password I used for the `floodit` PostgreSQL user

You can change it with the following command:

```bash
$> sudo -u postgres psql -c '\password floodit'
```

### :boom: System debugging

You can display the last few lines of the logs of your `floodit` Systemd
service with the following command:

```bash
$> sudo systemctl status floodit
```

If you need more details, you can display the full logs with the following
command:

```bash
$> sudo journalctl -u floodit
```

> :gem: You can scroll in `journalctl` logs using the up/down arrow keys, jump
> directly to the bottom with `Shift-G` (uppercase G), or back to the top with
> `G` (lowercase g). Exit with `Q` or `Ctrl-C`.

If the application does not seem to work after running the Systemd service,
there might be an error message in these logs that can help you identify the
issue.

### :boom: PostgreSQL debugging

You can list available databases with the following command:

```bash
$> sudo -u postgres psql -l
```

You can connect to a database with the following command:

```bash
$> sudo -u postgresql psql <database-name>
floodit=#
```

Note that the prompt has changed, because you are now connected to the
interactive PostgreSQL console. You can obtain help by typing the `\?` command
(`q` to exit the help page), or type SQL queries. For example, here's how to
list the tables in the current database and count the number of rows in the
`games` table:

```
floodit=# \d
                 List of relations
 Schema |         Name          |   Type   | Owner
--------+-----------------------+----------+--------
 public | flyway_schema_history | table    | floodit
 public | games                 | table    | floodit
 public | games_id_seq          | sequence | floodit
 public | moves                 | table    | floodit
 public | moves_id_seq          | sequence | floodit
(5 rows)

floodit=# select count(*) from games;
 count
-------
     2
(1 row)
```

Run the `exit` command when you are done to exit the PostgreSQL console.

### :boom: Updating your fork of the repository

If changes (e.g. bugfixes) are made to the original repository after you have
started the exercise, these changes will not automatically be included into your
fork of the repository. You can follow this procedure to update it.

**On your local machine:**

```bash
# Clone your fork of the Flood It repository on your local machine (replace
# MyGitHubUser by your GitHub username)
cd /path/to/projects
git clone git@github.com:MyGitHubUser/floodit.git
cd floodit

# Add a remote to the original repository
git remote add upstream https://github.com/MediaComem/floodit.git

# Fetch the latest changes from all remotes
git fetch --all

# Merge the latest changes from the original repository into your local repository
git merge upstream/main

# Push the new version to your fork on GitHub
git push origin main
```

If you have already setup the automated deployment, you simply need to push to
your `archidep` remote again.

Otherwise if you have cloned the repository on your server, you should also
update it. **Connect to your server** and run the following commands:

```bash
# Move into the floodit repository you have cloned
cd floodit

# Pull the latest changes
git pull
```

### :boom: `Error creating new order :: too many certificates already issued for: archidep.ch`

If you see an error similar to this when attempting to obtain a Let's Encrypt
TLS certificate with Certbot:

```bash
$> sudo certbot --nginx
Saving debug log to /var/log/letsencrypt/letsencrypt.log
Plugins selected: Authenticator nginx, Installer nginx

Which names would you like to activate HTTPS for?
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
1: clock.john-doe.archidep.ch
2: floodit.john-doe.archidep.ch
3: todolist.john-doe.archidep.ch
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
Select the appropriate numbers separated by commas and/or spaces, or leave input
blank to select all options shown (Enter 'c' to cancel): 2
Obtaining a new certificate
An unexpected error occurred:
There were too many requests of a given type :: Error creating new order :: too many certificates already issued for: archidep.ch: see https://letsencrypt.org/docs/rate-limits/
Please see the logfiles in /var/log/letsencrypt for more details.
```

It means that you are running into a [rate limit of the Let's Encrypt
service](https://letsencrypt.org/docs/rate-limits/): at most 50 certificates can
be requested per domain per week. With more than 50 students in both classes of
the architecture & deployment course, we may encounter this limit now and then.

A second domain, `archidep2.ch`, is now available for you. You can access
[Gandi.net](https://admin.gandi.net/domain/ef323400-0433-11e9-83f1-00163e4e76d5/)
to add entries to its DNS zone like in [the DNS
exercise](https://github.com/MediaComem/comem-archidep/blob/main/ex/dns-configuration.md#connect-to-gandinet).

You should add the same entries you added for `archidep.ch` to this new
`archidep2.ch` domain:

- An `A` entry for `john-doe` (replacing `john-doe` with
  your name) pointing to your server's public IP address.
- Another `A` entry for `*.john-doe` (replacing `john-doe` with your name)
  pointing to the same IP address.

You can then connect to your server and perform the following actions:

- Update your nginx configuration for the exercise to use your new subdomain of
  `archidep2.ch` instead of `archidep.ch`.
- Test and reload nginx's configuration with `sudo nginx -t` and `sudo nginx -s reload`.
- Run the `sudo certbot --nginx` command again. There should be no errors this
  time.

> Please notify the teacher immediately if you encounter the same error with the
> `archidep2.ch` domain.

### :boom: My changes to `config/local.exs` are not taken into account

Contrary to environment variables, the `config/local.exs` file is not read at
runtime (when the application starts), but is embedded into the application at
compilation time.

If you are running the application in development mode with `mix phx.server`,
re-compilation is done automatically for you, meaning it will always use the
latest version of the `config/local.exs` file.

Once you have created the production release with `MIX_ENV=prod mix release`,
the version of the `config/local.exs` file that existed when you created the
release has been included into the compiled release. Any subsequent changes to
the original file will not be taken into account unless you _re-create the
release_.

Unlike with Apache & PHP, Elixir code (or Ruby code, or Python code, etc) is not
reloaded automatically on each request. You must _also restart the application_
to take your changes into account.

[angular]: https://angular.io
[archidep-exercises]: https://github.com/MediaComem/comem-archidep#exercises
[auto-deploy-ex]: https://github.com/MediaComem/comem-archidep/blob/master/ex/git-automated-deployment.md
[automated-deployment-nginx-update]: https://github.com/MediaComem/comem-archidep/blob/master/ex/git-automated-deployment.md#update-the-todolist-nginx-configuration
[automated-tests]: https://en.wikipedia.org/wiki/Test_automation
[certbot-ex]: certbot-deployment.md
[composer]: https://getcomposer.org
[fork]: https://docs.github.com/en/get-started/quickstart/fork-a-repo
[http-502]: https://httpstatuses.com/502
[initial-setup]: https://github.com/MediaComem/floodit#initial-setup
[java]: https://www.oracle.com/java/
[js]: https://en.wikipedia.org/wiki/JavaScript
[jvm]: https://en.wikipedia.org/wiki/Java_virtual_machine
[laravel]: https://laravel.com
[mvc]: https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller
[mvn]: https://maven.apache.org
[mvn-central]: https://search.maven.org
[nginx-php-fpm-ex]: nginx-php-fpm-deployment.md
[nginx-proxy-pass]: http://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_pass
[nginx-rp-conf]: https://mediacomem.github.io/comem-archidep/2020-2021/subjects/reverse-proxy/?home=MediaComem%2Fcomem-archidep%23readme#29
[node]: https://nodejs.org
[node-install]: https://github.com/nodesource/distributions/blob/master/README.md
[node-lts]: https://nodejs.org/en/about/releases/
[npm]: https://www.npmjs.com
[openjdk]: https://openjdk.org
[oracle]: https://www.oracle.com
[orm]: https://en.wikipedia.org/wiki/Object%E2%80%93relational_mapping
[php]: https://www.php.net
[postgres]: https://www.postgresql.org
[spring]: https://spring.io
[spring-boot]: https://spring.io/projects/spring-boot
[readme]: https://github.com/MediaComem/floodit#readme
[repo]: https://github.com/MediaComem/floodit
[sun]: https://en.wikipedia.org/wiki/Sun_Microsystems
[systemd-ex]: systemd-deployment.md
[url]: https://en.wikipedia.org/wiki/URL#Syntax
[visudo]: https://linux.die.net/man/8/visudo
[webpack]: https://webpack.js.org
[webpack-caching]: https://webpack.js.org/guides/caching/
