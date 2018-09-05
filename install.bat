@echo.
@echo ----------------------------------------------------------------
@echo ----------------- Cool-RPC update master -----------------------
@echo ----------------------------------------------------------------

git pull origin master
git checkout master

@echo.
@echo ----------------------------------------------------------------
@echo ------------------ Cool-RPC  start task ------------------------
@echo ----------------------------------------------------------------

call mvn package

@echo.
@echo ----------------------------------------------------------------
@echo --------------- Cool-RPC  package complete ---------------------
@echo ----------------------------------------------------------------
@echo.

call mvn install:install-file -Dfile=target/cool-rpc-1.0.0.jar -DgroupId=com.cool -DartifactId=cool-rpc -Dversion=1.0.0 -Dpackaging=jar -DgeneratePom=true

@echo.
@echo -------------------------------------------------------------------------
@echo ----------- Cool-RPC  have been put into maven repository ---------------
@echo -------------------------------------------------------------------------
@echo.

call rd /s /Q target

@echo.
@echo Please add dependency in pom file
@echo.
@echo.
@echo.
@echo ^<dependency^>
@echo     ^<groupId^>com.cool^</groupId^>
@echo     ^<artifactId^>cool-rpc^</artifactId^>
@echo     ^<version^>1.0.0^</version^>
@echo ^</dependency^>
@echo.
@echo.
@echo.

@pause