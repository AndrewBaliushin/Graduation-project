CREATE TABLE tblTerms (
id INTEGER PRIMARY KEY,
term VARCHAR (96) NOT NULL UNIQUE
);

CREATE TABLE tblDefinitions (
id INTEGER PRIMARY KEY,
definition VARCHAR (255) NOT NULL,
term_id INTEGER NOT NULL,
CONSTRAINT term_fk FOREIGN KEY(term_id) REFERENCES tblTerms (id)
);

INSERT INTO tblTerms VALUES(1, 'ACID');
INSERT INTO tblDefinitions VALUES(1, 'The acronym for the four properties guaranteed by transactions: atomicity, consistency, isolation, and durability.', 1);
INSERT INTO tblTerms VALUES(2, 'API');
INSERT INTO tblDefinitions VALUES(2, 'Application Programming Interface. The specification of how a programmer writing an application accesses the behavior and state of	classes and objects.', 2);
INSERT INTO tblTerms VALUES(3, 'ASCII');
INSERT INTO tblDefinitions VALUES(3, 'American Standard Code for Information Interchange. A standard assignment of 7-bit numeric codes to characters. See also Unicode.', 3);
INSERT INTO tblTerms VALUES(4, 'Abstract Window Toolkit (AWT)');
INSERT INTO tblDefinitions VALUES(4, 'A collection of graphical user interface (GUI) components that were implemented using native-platform versions of the components.', 4);
INSERT INTO tblTerms VALUES(5, 'abstract');
INSERT INTO tblDefinitions VALUES(5, 'A Java keyword used in a class definition to specify that a class is not to be instantiated, but rather inherited by other classes. An abstract class can have abstract methods that are not implemented in the abstract class, but in subclasses.', 5);
INSERT INTO tblTerms VALUES(6, 'abstract class');
INSERT INTO tblDefinitions VALUES(6, 'A class that contains one or more abstract methods, and therefore can never be instantiated. Abstract classes are defined so that other classes can extend them and make them concrete by implementing the abstract methods.', 6);
INSERT INTO tblTerms VALUES(7, 'abstract method');
INSERT INTO tblDefinitions VALUES(7, 'A method that has no implementation.', 7);
INSERT INTO tblTerms VALUES(8, 'access control');
INSERT INTO tblDefinitions VALUES(8, 'The methods by which interactions with resources are limited to collections of users or programs for the purpose of enforcing integrity, confidentiality, or availability constraints.', 8);
INSERT INTO tblTerms VALUES(9, 'actual parameter list');
INSERT INTO tblDefinitions VALUES(9, 'The arguments specified in a particular method call. See also formal parameter list.', 9);
INSERT INTO tblTerms VALUES(10, 'applet');
INSERT INTO tblDefinitions VALUES(10, 'A component that typically executes in a Web browser, but can execute in a variety of other applications or devices that support the applet programming model.', 10);
INSERT INTO tblTerms VALUES(11, 'argument');
INSERT INTO tblDefinitions VALUES(11, 'A data item specified in a method call. An argument can be a literal value, a variable, or an expression.', 11);
INSERT INTO tblTerms VALUES(12, 'array');
INSERT INTO tblDefinitions VALUES(12, 'A collection of data items, all of the same type, in which each items position is uniquely designated by an integer.', 12);
INSERT INTO tblTerms VALUES(13, 'atomic');
INSERT INTO tblDefinitions VALUES(13, 'Refers to an operation that is never interrupted or left in an incomplete state under any circumstance.', 13);
INSERT INTO tblTerms VALUES(14, 'authentication');
INSERT INTO tblDefinitions VALUES(14, 'The process by which an entity proves to another entity that it is acting on behalf of a specific identity.', 14);
INSERT INTO tblTerms VALUES(15, 'authorization');
INSERT INTO tblDefinitions VALUES(15, 'See access control.', 15);
INSERT INTO tblTerms VALUES(16, 'autoboxing');
INSERT INTO tblDefinitions VALUES(16, 'Automatic conversion between reference and primitive types.', 16);
INSERT INTO tblTerms VALUES(17, 'bean');
INSERT INTO tblDefinitions VALUES(17, 'A reusable software component that conforms to certain design and naming conventions. The conventions enable beans to be easily combined to create an application using tools that understand the conventions.', 17);
INSERT INTO tblTerms VALUES(18, 'binary operator');
INSERT INTO tblDefinitions VALUES(18, 'An operator that has two arguments.', 18);
INSERT INTO tblTerms VALUES(19, 'bit');
INSERT INTO tblDefinitions VALUES(19, 'The smallest unit of information in a computer, with a value of either 0 or 1.', 19);
INSERT INTO tblTerms VALUES(20, 'bitwise operator');
INSERT INTO tblDefinitions VALUES(20, 'An operator that manipulates the bits of one or more of its operands individually and in parallel.', 20);
INSERT INTO tblTerms VALUES(21, 'block');
INSERT INTO tblDefinitions VALUES(21, 'In the Java programming language, any code between matching braces.', 21);
INSERT INTO tblTerms VALUES(22, 'boolean');
INSERT INTO tblDefinitions VALUES(22, 'Refers to an expression or variable that can have only a true or false value. The Java programming language provides the boolean type and the literal values true and false.', 22);
INSERT INTO tblTerms VALUES(23, 'break');
INSERT INTO tblDefinitions VALUES(23, 'A Java keyword used to resume program execution at the statement immediately following the current statement. If followed by a label, the program resumes execution at the labeled statement.', 23);
INSERT INTO tblTerms VALUES(24, 'byte');
INSERT INTO tblDefinitions VALUES(24, 'A sequence of eight bits. Java provides a corresponding byte type.', 24);
INSERT INTO tblTerms VALUES(25, 'bytecode');
INSERT INTO tblDefinitions VALUES(25, 'Machine-independent code generated by the Java compiler and executed by the Java interpreter.', 25);
INSERT INTO tblTerms VALUES(26, 'case');
INSERT INTO tblDefinitions VALUES(26, 'A Java keyword that defines a group of statements to begin executing if a value specified matches the value defined by a preceding switch keyword.', 26);
INSERT INTO tblTerms VALUES(27, 'term');
INSERT INTO tblDefinitions VALUES(27, 'abstract term', 27);
INSERT INTO tblDefinitions VALUES(28, 'spring term', 27);
INSERT INTO tblDefinitions VALUES(29, 'office term', 27);
