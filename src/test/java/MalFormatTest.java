class MalFormatTest {
    // @Test
    // void testMissingBrackets() {
    //     String input = "{nome=Fulano de Tal da Silva Sauro";
    //     CompactoParser parser = new CompactoParser();
    //     try {
    //         parser.parseObject(input, new Pessoa());
    //     } catch (Exception e) {
    //         assertTrue(e instanceof MalFormatException);
    //         assertTrue(e.getMessage().contains("Missing closing character in input: '"));
    //         assertEquals("'}' is missing.", e.getCause().getMessage());
    //     }
    // }

    // @Test
    // void testMissingBracketsInList() {
    //     String input = "{hobbies=[pular-corda}";
    //     CompactoParser parser = new CompactoParser();
    //     try {
    //         parser.parseObject(input, new Pessoa());
    //     } catch (Exception e) {
    //         assertTrue(e instanceof MalFormatException);
    //         assertTrue(e.getMessage().contains("Missing closing character in input: '"));
    //         assertEquals("']' is missing.", e.getCause().getMessage());
    //     }
    // }

    // @Test
    // void testMissingVariableName() {
    //     String input = "{=Fulano de Tal da Silva Sauro}";
    //     CompactoParser parser = new CompactoParser();
    //     try {
    //         parser.parseObject(input, new Pessoa());
    //     } catch (Exception e) {
    //         assertTrue(e instanceof MalFormatException);
    //         assertTrue(e.getMessage().contains("Missing field name in input: '"));
    //         assertEquals("field name is missing.", e.getCause().getMessage());
    //     }
    // }

    // @Test
    // void testMalFormedCompactoFormat() {
    //     String input = "{list=[[{object=Roberta}, {object=Joaquim}],[{object=Josélia}, {object=Luma}]]}";
    //     CompactoParser parser = new CompactoParser();
    //     try {
    //         parser.parseObject(input, new ListOfListOfStringObjectEncapsulator());
    //     } catch (Exception e) {
    //         assertTrue(e instanceof MalFormatException);
    //         assertTrue(e.getMessage().contains("Invalid field '{object' in input: "));
    //         assertEquals("'{object' is an invalid field name.", e.getCause().getMessage());
    //     }
    // }

    // @Test
    // void testInvalidVarName() {
    //     assertAll("Invalid var names", () -> {
    //         String input = "{list=[[{object=Roberta},{object=Joaquim}],[{-object=Josélia},{object=Luma}]]}";
    //         CompactoParser parser = new CompactoParser();
    //         try {parser.parseObject(input, new ListOfListOfStringObjectEncapsulator());
    //         } catch (Exception e) {assertTrue(e instanceof MalFormatException);assertTrue(e.getMessage().contains("-object"));assertTrue(e.getMessage().startsWith("Invalid field '-object' in input: "));assertEquals("'-object' is an invalid field name.", e.getCause().getMessage());
    //         }
    //     }, () -> {
    //         String input = "{list=[[{object=Roberta},{object=Joaquim}],[{*object=Josélia},{object=Luma}]]}";
    //         CompactoParser parser = new CompactoParser();
    //         try {parser.parseObject(input, new ListOfListOfStringObjectEncapsulator());
    //         } catch (Exception e) {assertTrue(e instanceof MalFormatException);assertTrue(e.getMessage().contains("*object"));assertTrue(e.getMessage().startsWith("Invalid field '*object' in input: "));assertEquals("'*object' is an invalid field name.", e.getCause().getMessage());
    //         }
    //     }, () -> {
    //         String input = "{list=[[{object=Roberta},{object=Joaquim}],[{$object=Josélia},{object=Luma}]]}";
    //         CompactoParser parser = new CompactoParser();
    //         try {parser.parseObject(input, new ListOfListOfStringObjectEncapsulator());
    //         } catch (Exception e) {assertTrue(e instanceof MalFormatException);assertTrue(e.getMessage().contains("$object"));assertTrue(e.getMessage().startsWith("Invalid field '$object' in input: "));assertEquals("'$object' is an invalid field name.", e.getCause().getMessage(), "Failed by $");
    //         }
    //     }, () -> {
    //         String input = "{list=[[{object=Roberta},{object=Joaquim}],[{@object=Josélia},{object=Luma}]]}";
    //         CompactoParser parser = new CompactoParser();
    //         try {parser.parseObject(input, new ListOfListOfStringObjectEncapsulator());
    //         } catch (Exception e) {assertTrue(e instanceof MalFormatException);assertTrue(e.getMessage().contains("@object"));assertTrue(e.getMessage().startsWith("Invalid field '@object' in input: "));assertEquals("'@object' is an invalid field name.", e.getCause().getMessage(), "Failed by @");
    //         }
    //     }, () -> {
    //         String input = "{list=[[{object=Roberta},{object=Joaquim}],[{obj ect=Josélia},{object=Luma}]]}";
    //         CompactoParser parser = new CompactoParser();
    //         try {parser.parseObject(input, new ListOfListOfStringObjectEncapsulator());
    //         } catch (Exception e) {assertTrue(e instanceof MalFormatException);assertTrue(e.getMessage().contains("obj ect"));assertTrue(e.getMessage().startsWith("Invalid field 'obj ect' in input: "));assertEquals("'obj ect' is an invalid field name.", e.getCause().getMessage(),        "Failed by splitted var");
    //         }
    //     }, () -> {
    //         String input = "{list=[[{object=Roberta},{object=Joaquim}],[{5object=Josélia},{object=Luma}]]}";
    //         CompactoParser parser = new CompactoParser();
    //         try {parser.parseObject(input, new ListOfListOfStringObjectEncapsulator());
    //         } catch (Exception e) {assertTrue(e instanceof MalFormatException);assertTrue(e.getMessage().contains("5object"));assertTrue(e.getMessage().startsWith("Invalid field '5object' in input: "));assertEquals("'5object' is an invalid field name.",        e.getCause().getMessage(), "Failed by 5object");
    //         }
    //     });
    // }
}
