/*
 * Copyright (c) 2020, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Efficient Java Matrix Library (EJML).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ejml;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;


/**
 * <p>Base class for code generators.</p>
 *
 * @author Peter Abeles
 */
@SuppressWarnings("NullAway.Init")
public abstract class CodeGeneratorBase {

    public static final String copyright =
            "/*\n" +
                    " * Copyright (c) 2020, Peter Abeles. All Rights Reserved.\n" +
                    " *\n" +
                    " * This file is part of Efficient Java Matrix Library (EJML).\n" +
                    " *\n" +
                    " * Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    " * you may not use this file except in compliance with the License.\n" +
                    " * You may obtain a copy of the License at\n" +
                    " *\n" +
                    " *   http://www.apache.org/licenses/LICENSE-2.0\n" +
                    " *\n" +
                    " * Unless required by applicable law or agreed to in writing, software\n" +
                    " * distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    " * See the License for the specific language governing permissions and\n" +
                    " * limitations under the License.\n" +
                    " */\n";

    protected PrintStream out;
    protected String className;

    /**
     * Creates the code
     */
    public abstract void generate() throws FileNotFoundException;

    protected String standardClassDocClosing(String ...authors) {
        return  " *\n" +
                " * <p>DO NOT MODIFY. Automatically generated code created by "+getClass().getSimpleName()+"</p>\n" +
                " *\n" +
                " * @author "+authors[0]+"\n" + // yes I was lazy here..
                " */\n" +
                "@Generated(\""+getClass().getCanonicalName()+"\")\n";
    }

    public void setOutputFile( String className ) throws FileNotFoundException {
        this.className = className;
        out = new PrintStream(new FileOutputStream(className + ".java"));
        out.print(copyright);
        out.println();
        out.println("package " + getPackage() + ";");
        out.println();
        out.println("import javax.annotation.Generated;");
    }

    public String getPackage() {
        return getClass().getPackage().getName();
    }
}
