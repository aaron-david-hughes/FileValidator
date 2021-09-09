<h1>File Syntax Validator</h1>
<p>
    This project comes with some predefined file syntax validators:
</p>
<ul>
    <li>Json</li>
    <li>Binary</li>
</ul>

<p>
    You can add you own file types by simply doing the following steps:
</p>
<ol>
    <li>Define an enum of states implementing the State interface.</li>
    <li>Define a class of state transitions implementing the TuringMachine interface.</li>
    <li>Create a validator with the TuringMachine class and the start state and register to via FileTypeRegistry.addFileType.</li>
    <li>Call FileValidation.runValidation to use.</li>
</ol>

<h2>
    ε-non-deterministic Turing Machine of default Json validation
</h2>
<p>
    The below diagram indicates the flow of validation 
    of json files character by character:
</p>

```mermaid
graph TD;
    START --> |"char  {, push OBJECT"| OBJECT_ELEMENT_START;
    START --> |"char [, push ARRAY"| ARRAY_ELEMENT_START;
    OBJECT_ELEMENT_START --> |"char #quot;"| KEY;
    OBJECT_ELEMENT_START --> |"char }, pop OBJECT"| ELEMENT_END;
    ARRAY_ELEMENT_START --> |"char [, push ARRAY"| ARRAY_ELEMENT_START;
    ARRAY_ELEMENT_START --> |"char {, push OBJECT"| OBJECT_ELEMENT_START;
    ARRAY_ELEMENT_START --> |"char #quot;"| STRING;
    ARRAY_ELEMENT_START --> |"char ], pop ARRAY"| ELEMENT_END;
    DELIMINATOR --> |"char :"| VALUE;
    VALUE --> |"char {, push OBJECT"| OBJECT_ELEMENT_START;
    VALUE --> |"char [, push ARRAY"| ARRAY_ELEMENT_START;
    VALUE --> |"char #quot;"| STRING;
    VALUE --> |"char 0-9"| WHOLE_NUMBER;
    ELEMENT_END --> |"char ' '(space), has empty stack"| ACCEPT;
    ELEMENT_END --> |"char ,"| HAS_NEXT;
    ELEMENT_END --> |"char }, pop OBJECT"| ELEMENT_END;
    ELEMENT_END --> |"char ], pop ARRAY"| ELEMENT_END;
    KEY --> |"char \, set isKey = true"| ESCAPE_CHAR;
    KEY --> |"char #quot;, set isKey = true"| DELIMINATOR;
    KEY --> |"other chars, set isKey = true"| KEY;
    STRING --> |"char \, set isKey = false"| ESCAPE_CHAR;
    STRING --> |"char #quot;, set isKey = false"| ELEMENT_END;
    STRING --> |"other chars, set isKey = false"| STRING;
    ESCAPE_CHAR --> |"char #quot;, if isKey = false"| STRING;
    ESCAPE_CHAR --> |"char b, if isKey = false"| STRING;
    ESCAPE_CHAR --> |"char n, if isKey = false"| STRING;
    ESCAPE_CHAR --> |"char t;, if isKey = false"| STRING;
    ESCAPE_CHAR --> |"char \;, if isKey = false"| STRING;
    ESCAPE_CHAR --> |"char #quot;, if isKey = true"| KEY;
    ESCAPE_CHAR --> |"char b, if isKey = true"| KEY;
    ESCAPE_CHAR --> |"char n, if isKey = true"| KEY;
    ESCAPE_CHAR --> |"char t;, if isKey = true"| KEY;
    ESCAPE_CHAR --> |"char \;, if isKey = true"| KEY;
    HAS_NEXT --> |"char {, push OBJECT"| OBJECT_ELEMENT_START;
    HAS_NEXT --> |"char [, push ARRAY"| ARRAY_ELEMENT_START;
    HAS_NEXT --> |"char #quot;, if isKey = true"| KEY;
    HAS_NEXT --> |"char #quot;, if isKey = false"| STRING;
    HAS_NEXT --> |"char 0-9, if isKey = false"| WHOLE_NUMBER;
    WHOLE_NUMBER --> |"char 0-9"| WHOLE_NUMBER;
    WHOLE_NUMBER --> |"char ."| DECIMAL_NUMBER_REQUIRED;
    WHOLE_NUMBER --> |"char ε"| ELEMENT_END;
    DECIMAL_NUMBER_REQUIRED --> |"char 0-9"| DECIMAL_NUMBER_OPTIONAL;
    DECIMAL_NUMBER_OPTIONAL --> |"char 0-9"| DECIMAL_NUMBER_OPTIONAL;
    DECIMAL_NUMBER_OPTIONAL --> |"char ε"| ELEMENT_END;
    
    
    style ACCEPT fill:#f9f,stroke:#333,stroke-width:4px;
    style START fill:#9f9,stroke:#333,stroke-width:4px;
    style OBJECT_ELEMENT_START fill:#9f9,stroke:#333,stroke-width:4px;
    style ARRAY_ELEMENT_START fill:#9f9,stroke:#333,stroke-width:4px;
    style DELIMINATOR fill:#9f9,stroke:#333,stroke-width:4px;
    style VALUE fill:#9f9,stroke:#333,stroke-width:4px;
    style ELEMENT_END fill:#9f9,stroke:#333,stroke-width:4px;
    style KEY fill:#9f9,stroke:#333,stroke-width:4px;
    style STRING fill:#9f9,stroke:#333,stroke-width:4px;
    style ESCAPE_CHAR fill:#9f9,stroke:#333,stroke-width:4px;
    style HAS_NEXT fill:#9f9,stroke:#333,stroke-width:4px;
    style WHOLE_NUMBER fill:#9f9,stroke:#333,stroke-width:4px;
    style DECIMAL_NUMBER_REQUIRED fill:#9f9,stroke:#333,stroke-width:4px;
    style DECIMAL_NUMBER_OPTIONAL fill:#9f9,stroke:#333,stroke-width:4px;
```

<h2>
    ε-non-deterministic Turing Machine of default Binary Validation
</h2>
<p>
    The below diagram indicates the flow of validation 
    of binary files character by character:
</p>

```mermaid
graph TD;
    START --> |"char 0"| ZERO;
    START --> |"char 1"| ONE;
    START --> |"char ' '(space)"| ACCEPT;
    ZERO --> |"char 0"| ZERO;
    ZERO --> |"char 1"| ONE;
    ZERO --> |"char ' '(space)"| ACCEPT;
    ONE --> |"char 0"| ZERO;
    ONE --> |"char 1"| ONE;
    ONE --> |"char ' '(space)"| ACCEPT;
    
    style ACCEPT fill:#f9f,stroke:#333,stroke-width:4px;
    style START fill:#9f9,stroke:#333,stroke-width:4px;
    style ONE fill:#9f9,stroke:#333,stroke-width:4px;
    style ZERO fill:#9f9,stroke:#333,stroke-width:4px;
```

<p>
    Note:
</p>
<ul>
    <li>
        any unspecified character movements are to an implicit reject state, 
        which theoretically loops to stay there (In reality short circuits 
        the machine to avoid pointless computations)
    </li>
    <li>
        an explicit accept transition is required, working on a transition of the
        space character at the end of the file contents. This space is added by the
        validator, but you will be required to cover the state transition.
    </li>
</ul>