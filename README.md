# datamask - Pseudonymization in Java

Pseudonymization is a reversible data-masking technique that permits to mask some data and retrieve the original value
using the cryptographic key previously used.

This library is using both Format-preserving encryption (FPE) and AES algorithm to perform several pseudonymization
operation to fields of Entities that are marked with custom JAVA annotation.

```java
@DataCrypt
```

The encryption (or decryption) is done using java-reflection.

## Installation

You can pull it from the central Maven repositories:

```xml

```

## Example Usage

### Code

1) In order to start, we have to specify the DataType we want to use for our Entity's field.

```java

@Entity
@Table(name = "amounts")
public class Amounts {

    @Id
    public String code;

    @DataCrypt(dataType = DataCrypt.DataType.AMOUNT)
    public BigDecimal price;

    public String job;

    public BigDecimal originalPrice;

}
```

2) After that, we create a bean of Pseudonymize class with AES keys (IV and Secret)

```java
@Bean
public Pseudonymize pseudonymize(String IV,String SECRET)
        throws NoSuchPaddingException,UnsupportedEncodingException,NoSuchAlgorithmException{
        return new Pseudonymize(IV,SECRET);
        }
```

3) In order to crypt (or decrypt) our Entity, we pass its reference.

```java
//crypt
pseudonymize.cryptClass(entity);
//decrypt
pseudonymize.decryptClass(entity);
```

## Requirements

The library has been tested with _Apache Maven 3.8.3_ and _JDK 1.8_.

## License

Read [LICENSE.txt] (LICENSE) attached to the project