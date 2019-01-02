import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap

def Message browse(Message message) {
    def mapHeaders = message.getHeaders()
    
    String mainDir = mapHeaders.get("CamelHttpPath") 
    String mainUrl = mapHeaders.get("CamelHttpUrl") 
    String mainSrv = mapHeaders.get("CamelServletContextPath")
    String srvUrl  = mainUrl.minus(mainDir)
    String urlDown = srvUrl.minus(mainSrv)+"/amba/cpifilesystemdownload"
    
    File dir = new File(mainDir)
    StringBuilder strBuilder = new StringBuilder()
    strBuilder << '<meta charset="UTF-8"></br>'
    if (dir.parent != null){strBuilder << "<a href='${srvUrl}${dir.parent}'>&#x1F4C1 ..</a></br>"}
    dir.eachDir {strBuilder << "<a href='${mainUrl}/${it.name}'>&#x1F4C1 ${it.name}</a></br>"}
    dir.eachFile {if (it.isFile()) {strBuilder << "<a href='${urlDown}/${mainDir}/${it.name}'>${it.name}</a></br>" }}
    message.setBody(strBuilder.toString())
    
    mapHeaders = ['content-type':'text/html']
    message.setHeaders(mapHeaders)
    return message
}

def Message download(Message message) {
    def mapHeaders = message.getHeaders()
    
    String filePath = mapHeaders.get("CamelHttpPath") 
    StringBuilder strBuilder = new StringBuilder()
    
    File file = new File(filePath);byte[] fileContent = file.bytes
    strBuilder << fileContent.encodeBase64().toString()    
    message.setBody(strBuilder.toString());
    
    mapHeaders = ['Content-Transfer-Encoding':'base64',
                  'Content-Disposition':'attachment; filename=${file.getName()}']
                  
                  
    return message
}

