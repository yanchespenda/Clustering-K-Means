$currentPath = (get-item -Path ".\" -Verbose).FullName

$outputPath = Join-Path $currentPath "fixed"
if (!(Test-Path $outputPath)) {
    New-Item -ItemType Directory -Path $outputPath | Out-Null
}
$count = 0
Get-ChildItem $currentPath -Filter "*.csv" | 
Foreach-Object {
    $content = Get-Content $_.FullName
    
    #remove double quotes at the start and end of line
    $startAndEnd = [regex]("^`"|`"$")

    #remove double quotes before and after commas
    $aroundCommas = [regex]("`",`"|`",|,`"")
    
    #filter and save content to a new file 
    $content | Foreach-Object {$_ -replace $startAndEnd, ''`
        -replace $aroundCommas, ','} `
        | Set-Content ($outputPath + "\" + $_.Name)
    $count++
}
Write-Host "Fixed file count: "$count