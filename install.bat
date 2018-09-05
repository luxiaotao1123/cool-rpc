@echo.
@echo ----------------------------------------------------------------
@echo -------------------Cool-RPC  打包任务---------------------------
@echo ----------------------------------------------------------------

call mvn package

@echo.
@echo ----------------------------------------------------------------
@echo ---------------------Cool-RPC  打包完成-------------------------
@echo ----------------------------------------------------------------
@echo.

call mvn install:install-file -Dfile=target/cool-rpc-1.0.0.jar -DgroupId=com.cool -DartifactId=cool-rpc -Dversion=1.0.0 -Dpackaging=jar -DgeneratePom=true

@echo.
@echo -------------------------------------------------------------------------
@echo -----------------------Cool-RPC  已导入本地仓库--------------------------
@echo -------------------------------------------------------------------------
@echo.

call rd /s /Q target

@echo.
@echo 请在项目pom文件中添加以下依赖：
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