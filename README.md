 // Vòng lặp con xử lý các phần tử trong trang hiện tại
            int index = 1;
            for (int j = 1; j <= 10; j++) {
                try {
                    dvcbnOddOrEven(webDriverWait, true, j);
                } catch (Exception e) {
                    log.error("Loi trong qua trinh crawler: " + e.getMessage());
                }
                try {
                    dvcbnOddOrEven(webDriverWait, false, j);
                } catch (Exception e) {
                    log.error("Loi trong qua trinh crawler: " + e.getMessage());
                }

            }


        }


    }


    public void dvcbnOddOrEven(WebDriverWait webDriverWait, Boolean odd, int index) {

        String xpath;
        if (odd) {
            xpath = String.format("//tr[@class='odd'][%d]/td[@class='text-center']//button[@class='btn btn-info']", index);
        } else {
            xpath = String.format("//tr[@class='even'][%d]/td[@class='text-center']//button[@class='btn btn-info']", index);
        }


        // Xử lý nút click
        WebElement elementClick = webDriverWait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(xpath)
        ));
        elementClick.click();


        List<IdDichVucCongBacNinh> idDichVucCongBacNinhList = idDichVucCongBacNinhRepository.findAll();

        // check xem neu da ton tai id chua neu ton tai r bo qua

        // Tiếp tục lấy ra phần tử thông tin

        WebElement webElementParent = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[@class='table table-hover']/tbody")
        ));


        //-------------lấy mã thủ tục
        WebElement elementMaThuTucValue = webElementParent.findElement(By.xpath(".//tr[1]/td"));
        //xử lý mã thủ tục bỏ chữ xem chi tiết đi
        String elementMaThuTucText = elementMaThuTucValue.getText().replaceAll("Xem chi tiết", "").trim();
        String stringBuilderMaThuTuc = Constant.MA_THU_TUC + elementMaThuTucText;
        Boolean checkExistsMaThuTuc = false;
        // kiem tra xem ma thu tuc da ton tai
        for (IdDichVucCongBacNinh idDichVucCongBacNinh : idDichVucCongBacNinhList) {
            if (idDichVucCongBacNinh.getMaThuTuc().equals(elementMaThuTucText)) {
                checkExistsMaThuTuc = true;
                break;
            }
        }
